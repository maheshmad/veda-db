//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.27 at 08:43:41 AM EDT 
//


package com.taksila.veda.model.api.classroom.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;


/**
 * 
 * 				This represents the api structure of the GetEnrollmentResponse response 				
 * 			
 * 
 * <p>Java class for GetEnrollmentResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetEnrollmentResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="enrollment" type="{http://www.taksila.com/veda/model/api/classroom/v1_0}Enrollment"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetEnrollmentResponse", propOrder = {
    "enrollment"
})
public class GetEnrollmentResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected Enrollment enrollment;

    /**
     * Gets the value of the enrollment property.
     * 
     * @return
     *     possible object is
     *     {@link Enrollment }
     *     
     */
    public Enrollment getEnrollment() {
        return enrollment;
    }

    /**
     * Sets the value of the enrollment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Enrollment }
     *     
     */
    public void setEnrollment(Enrollment value) {
        this.enrollment = value;
    }

}
