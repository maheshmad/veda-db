//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.21 at 09:20:22 PM EDT 
//


package com.taksila.veda.model.api.classroom.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;


/**
 * 
 * 				This represents the api structure of the GetUserResponse response 				
 * 			
 * 
 * <p>Java class for GetClassroomResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetClassroomResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="classroom" type="{http://www.taksila.com/veda/model/api/classroom/v1_0}Classroom"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetClassroomResponse", propOrder = {
    "classroom"
})
public class GetClassroomResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected Classroom classroom;

    /**
     * Gets the value of the classroom property.
     * 
     * @return
     *     possible object is
     *     {@link Classroom }
     *     
     */
    public Classroom getClassroom() {
        return classroom;
    }

    /**
     * Sets the value of the classroom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Classroom }
     *     
     */
    public void setClassroom(Classroom value) {
        this.classroom = value;
    }

}
