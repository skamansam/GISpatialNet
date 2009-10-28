//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.28 at 12:49:25 PM EDT 
//


package us.jonesrychtar.gispatialnet.Reader.kml.opengis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import us.jonesrychtar.gispatialnet.Reader.kml.external.oasis.names.tc.ciq.xsdschema.xal.AddressDetails;
import us.jonesrychtar.gispatialnet.Reader.kml.external.org.w3.atom.AtomPersonConstruct;
import us.jonesrychtar.gispatialnet.Reader.kml.external.org.w3.atom.Link;


/**
 * <p>Java class for AbstractFeatureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractFeatureType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}AbstractObjectType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}name" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}visibility" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}open" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2005/Atom}author" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2005/Atom}link" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}address" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressDetails" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}phoneNumber" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.opengis.net/kml/2.2}Snippet" minOccurs="0"/>
 *           &lt;element ref="{http://www.opengis.net/kml/2.2}snippet" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractViewGroup" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractTimePrimitiveGroup" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}styleUrl" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractStyleSelectorGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}Region" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.opengis.net/kml/2.2}Metadata" minOccurs="0"/>
 *           &lt;element ref="{http://www.opengis.net/kml/2.2}ExtendedData" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractFeatureSimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractFeatureObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractFeatureType", propOrder = {
    "rest"
})
@XmlSeeAlso({
    NetworkLinkType.class,
    AbstractOverlayType.class,
    AbstractContainerType.class,
    PlacemarkType.class
})
public abstract class AbstractFeatureType
    extends AbstractObjectType
{

    @XmlElementRefs({
        @XmlElementRef(name = "AbstractViewGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "Metadata", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "snippet", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "AbstractFeatureSimpleExtensionGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "open", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "link", namespace = "http://www.w3.org/2005/Atom", type = Link.class),
        @XmlElementRef(name = "AddressDetails", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = JAXBElement.class),
        @XmlElementRef(name = "Snippet", namespace = "http://www.opengis.net/kml/2.2", type = SnippetDeprecated.class),
        @XmlElementRef(name = "Region", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "name", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "phoneNumber", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "styleUrl", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "description", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "AbstractTimePrimitiveGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "ExtendedData", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "AbstractFeatureObjectExtensionGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "visibility", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "author", namespace = "http://www.w3.org/2005/Atom", type = JAXBElement.class),
        @XmlElementRef(name = "address", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "AbstractStyleSelectorGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class)
    })
    protected List<Object> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Snippet" is used by two different parts of a schema. See: 
     * line 321 of http://schemas.opengis.net/kml/2.2.0/ogckml22.xsd
     * line 320 of http://schemas.opengis.net/kml/2.2.0/ogckml22.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link StyleMapType }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link TimeSpanType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegionType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link LookAtType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractStyleSelectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractViewType }{@code >}
     * {@link JAXBElement }{@code <}{@link MetadataType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link CameraType }{@code >}
     * {@link Link }
     * {@link JAXBElement }{@code <}{@link AddressDetails }{@code >}
     * {@link SnippetDeprecated }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link TimeStampType }{@code >}
     * {@link JAXBElement }{@code <}{@link StyleType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTimePrimitiveType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExtendedDataType }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link AtomPersonConstruct }{@code >}
     * 
     * 
     */
    public List<Object> getRest() {
        if (rest == null) {
            rest = new ArrayList<Object>();
        }
        return this.rest;
    }

}
