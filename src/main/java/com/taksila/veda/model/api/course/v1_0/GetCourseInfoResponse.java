//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.23 at 11:28:56 AM EDT 
//


package com.taksila.veda.model.api.course.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;


/**
 * 
 * 				This represents the api structure of the GetUserInfoResponse response 				
 * 			
 * 
 * <p>Java class for GetCourseInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCourseInfoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="course" type="{http://www.taksila.com/veda/model/api/course/v1_0}Course"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCourseInfoResponse", propOrder = {
    "course"
})
public class GetCourseInfoResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected Course course;

    /**
     * Gets the value of the course property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the value of the course property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setCourse(Course value) {
        this.course = value;
    }

}
