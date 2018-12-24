package com.taksila.veda.db.dao;

import java.util.List;

import com.taksila.veda.model.api.course.v1_0.Topic;

public interface TopicRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Topic> searchTopicsByTitle(String q) throws Exception;

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Topic> searchTopicsByChapterid(String chapterid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Topic getTopicById(String id) throws Exception;

	/**
	 * 
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	Topic insertTopic(Topic topic) throws Exception;

	/**
	 * 
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	boolean updateTopic(Topic topic) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteTopic(String id) throws Exception;

}