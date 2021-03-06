/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Axel (http://code.google.com/p/inera-axel).
 *
 * Inera Axel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Axel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package se.inera.axel.shs.xml.label;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "compound")
public class Compound implements Serializable {

    @XmlAttribute(name = "no-of-parts", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String noOfParts;

    /**
     * Gets the value of the noOfParts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoOfParts() {
        return noOfParts;
    }

    /**
     * Sets the value of the noOfParts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoOfParts(String value) {
        this.noOfParts = value;
    }

	@Override
	public String toString() {
		return "Compound{" +
				"noOfParts='" + noOfParts + '\'' +
				'}';
	}
}
