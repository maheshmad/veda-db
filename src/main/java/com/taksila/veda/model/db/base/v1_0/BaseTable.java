//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.27 at 08:48:14 AM EDT 
//


package com.taksila.veda.model.db.base.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.taksila.veda.model.db.classroom.v1_0.Classroom;
import com.taksila.veda.model.db.classroom.v1_0.Enrollment;
import com.taksila.veda.model.db.usermgmt.v1_0.User;
import com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo;


/**
 * 
 * 				Chapter table has list of chapters.
 * 				Each chapter will have list of topics								
 * 			
 * 
 * <p>Java class for BaseTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/base/v1_0}id"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/base/v1_0}updatedBy"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/base/v1_0}lastUpdatedDateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseTable", propOrder = {
    "id",
    "updatedBy",
    "lastUpdatedDateTime"
})
@XmlSeeAlso({
    Classroom.class,
    Enrollment.class,
    Activity.class,
    User.class,
    UserImageInfo.class
})
public class BaseTable {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String updatedBy;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdatedDateTime;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the updatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the value of the updatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

    /**
     * Gets the value of the lastUpdatedDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    /**
     * Sets the value of the lastUpdatedDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdatedDateTime(XMLGregorianCalendar value) {
        this.lastUpdatedDateTime = value;
    }

}
