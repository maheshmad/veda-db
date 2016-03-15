//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.15 at 12:09:26 AM EDT 
//


package com.taksila.veda.model.db.course.v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.course.v1_0.Chapter;


/**
 * 
 * 				Chapter table has list of chapters.
 * 				Each chapter will have list of topics								
 * 			
 * 
 * <p>Java class for ChapterTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChapterTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/course/v1_0}id"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/course/v1_0}name"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/course/v1_0}title"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/course/v1_0}subTitle"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/course/v1_0}description"/>
 *         &lt;element name="topics" type="{http://www.taksila.com/veda/model/db/course/v1_0}TopicTable" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChapterTable", propOrder = {
    "id",
    "name",
    "title",
    "subTitle",
    "description",
    "topics"
})
@XmlSeeAlso({
    Chapter.class
})
public class ChapterTable {

    protected int id;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String subTitle;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected List<TopicTable> topics;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the subTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Sets the value of the subTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubTitle(String value) {
        this.subTitle = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the topics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TopicTable }
     * 
     * 
     */
    public List<TopicTable> getTopics() {
        if (topics == null) {
            topics = new ArrayList<TopicTable>();
        }
        return this.topics;
    }

}
