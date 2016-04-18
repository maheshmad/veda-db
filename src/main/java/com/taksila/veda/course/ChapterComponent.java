package com.taksila.veda.course;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.ChapterDAO;
import com.taksila.veda.db.dao.TopicDAO;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.course.v1_0.Chapter;
import com.taksila.veda.model.api.course.v1_0.CreateChapterRequest;
import com.taksila.veda.model.api.course.v1_0.CreateChapterResponse;
import com.taksila.veda.model.api.course.v1_0.DeleteChapterRequest;
import com.taksila.veda.model.api.course.v1_0.DeleteChapterResponse;
import com.taksila.veda.model.api.course.v1_0.GetChapterRequest;
import com.taksila.veda.model.api.course.v1_0.GetChapterResponse;
import com.taksila.veda.model.api.course.v1_0.SearchChaptersRequest;
import com.taksila.veda.model.api.course.v1_0.SearchChaptersResponse;
import com.taksila.veda.model.api.course.v1_0.UpdateChapterRequest;
import com.taksila.veda.model.api.course.v1_0.UpdateChapterResponse;
import com.taksila.veda.utils.CommonUtils;


public class ChapterComponent 
{	
	private String schoolId =null;	
	private ChapterDAO chapterDAO = null;
	private TopicDAO topicDAO = null;
	static Logger logger = LogManager.getLogger(ChapterComponent.class.getName());
	
	public ChapterComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.chapterDAO = new ChapterDAO(this.schoolId);
		this.topicDAO = new TopicDAO(this.schoolId);
	}
			
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchChaptersResponse searchChapter(SearchChaptersRequest req)
	{
		SearchChaptersResponse resp = new SearchChaptersResponse();
		try 
		{
			List<Chapter> chapterSearchHits = chapterDAO.searchChaptersByTitle(req.getQuery());
			
			for(Chapter chapter: chapterSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(chapter.getId()));
				rec.setRecordTitle(chapter.getTitle());
				rec.setRecordSubtitle(chapter.getSubTitle());				
				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("CHAPTER");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(chapterSearchHits.size());

		} 
		catch (Exception e) 
		{			
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public GetChapterResponse getChapter(GetChapterRequest req)
	{
		GetChapterResponse resp = new GetChapterResponse();
		try 
		{
			Chapter chapter = chapterDAO.getChapterById(req.getId());
			
			if (chapter == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
				chapter.getTopics().addAll(this.topicDAO.searchTopicsByChapterid(req.getId()));
				resp.setChapter(chapter);
			}					

		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public CreateChapterResponse createNewChapter(CreateChapterRequest req)
	{
		CreateChapterResponse resp = new CreateChapterResponse();
		try 
		{							
			Chapter course = chapterDAO.insertChapter(req.getChapter());
			resp.setChapter(course);
			resp.setStatus(StatusType.SUCCESS);
			resp.setMsg("Successfully created a new chapter id = "+resp.getChapter().getId());
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public UpdateChapterResponse updateChapter(UpdateChapterRequest req)
	{
		UpdateChapterResponse resp = new UpdateChapterResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = chapterDAO.updateChapter(req.getChapter());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setChapter(req.getChapter());
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
			}
			
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public DeleteChapterResponse deleteChapter(DeleteChapterRequest req)
	{
		DeleteChapterResponse resp = new DeleteChapterResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = chapterDAO.deleteChapter(req.getId());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
			}
			
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
}
