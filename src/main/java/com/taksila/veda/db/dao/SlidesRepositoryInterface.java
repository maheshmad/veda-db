package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.taksila.veda.model.api.course.v1_0.Slide;

public interface SlidesRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Slide> searchSlidesByTitle(String q) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Slide getSlideById(String id) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	List<Slide> searchSlidesByTopicId(String topicid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Slide getSlideByName(String name) throws Exception;

	/**
	 * 
	 * @param slide
	 * @return
	 * @throws Exception
	 */
	Slide insertSlide(Slide slide) throws Exception;

	/**
	 * 
	 * @param slide
	 * @return
	 * @throws Exception
	 */
	boolean updateSlide(Slide slide) throws Exception;

	/**
	 * 
	 * @param slide
	 * @return
	 * @throws Exception
	 */
	boolean updateSlideImage(String slideId, InputStream slideContentImageIs, String imageType, double scale)
			throws Exception;

	/**
	 * 
	 * @param slide
	 * @return
	 * @throws Exception
	 */
	ByteArrayOutputStream readSlideImage(int slideId, double scale) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteSlide(String id) throws Exception;

}