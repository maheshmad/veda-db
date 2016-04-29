//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.27 at 08:43:41 AM EDT 
//


package com.taksila.veda.model.api.base.v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.classroom.v1_0.SearchClassroomResponse;
import com.taksila.veda.model.api.classroom.v1_0.SearchEnrollmentResponse;


/**
 * 
 * 				SearchResponse is a generic class for getting search results of 
 * 				any type of records				
 * 			
 * 
 * <p>Java class for SearchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}recordType"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}totalHits"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}page"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}pageOffset"/>
 *         &lt;element name="hits" type="{http://www.taksila.com/veda/model/api/base/v1_0}SearchHitRecord" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchResponse", propOrder = {
    "recordType",
    "totalHits",
    "page",
    "pageOffset",
    "hits"
})
@XmlSeeAlso({
    SearchEnrollmentResponse.class,
    SearchClassroomResponse.class
})
public class SearchResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected String recordType;
    protected int totalHits;
    protected int page;
    protected int pageOffset;
    protected List<SearchHitRecord> hits;

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordType(String value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the totalHits property.
     * 
     */
    public int getTotalHits() {
        return totalHits;
    }

    /**
     * Sets the value of the totalHits property.
     * 
     */
    public void setTotalHits(int value) {
        this.totalHits = value;
    }

    /**
     * Gets the value of the page property.
     * 
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the value of the page property.
     * 
     */
    public void setPage(int value) {
        this.page = value;
    }

    /**
     * Gets the value of the pageOffset property.
     * 
     */
    public int getPageOffset() {
        return pageOffset;
    }

    /**
     * Sets the value of the pageOffset property.
     * 
     */
    public void setPageOffset(int value) {
        this.pageOffset = value;
    }

    /**
     * Gets the value of the hits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchHitRecord }
     * 
     * 
     */
    public List<SearchHitRecord> getHits() {
        if (hits == null) {
            hits = new ArrayList<SearchHitRecord>();
        }
        return this.hits;
    }

}
