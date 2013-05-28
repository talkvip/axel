/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Axel (http://code.google.com/p/inera-axel).
 *
 * Inera Axel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Axel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package se.inera.axel.shs.agreement;

import java.util.List;

import se.inera.axel.shs.exception.ShsException;
import se.inera.axel.shs.xml.agreement.ShsAgreement;
import se.inera.axel.shs.xml.label.ShsLabel;

/**
 * Interface to agreements for use mainly by the broker in validating agreements and routing.
 * 
 * @author bjorn
 */
public interface AgreementService {

	ShsAgreement findOne(String agreementId);
    List<ShsAgreement> findAgreements(ShsLabel label);


	void validateAgreement(ShsLabel label) throws ShsException;
	
}
