//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.17 at 05:55:12 PM EDT 
//


package com.taksila.veda.model.api.course.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;


/**
 * 
 * 				This represents the api structure of the GetSlideInfoResponse response 				
 * 			
 * 
 * <p>Java class for UploadFileResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadFileResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="fileid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadFileResponse", propOrder = {
    "fileid"
})
public class UploadFileResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected String fileid;

    /**
     * Gets the value of the fileid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileid() {
        return fileid;
    }

    /**
     * Sets the value of the fileid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileid(String value) {
        this.fileid = value;
    }

}