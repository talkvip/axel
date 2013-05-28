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
package se.inera.axel.shs.cmdline;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.apache.camel.component.http.SSLContextParametersSecureProtocolSocketFactory;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import se.inera.axel.shs.camel.DefaultCamelToShsMessageProcessor;
import se.inera.axel.shs.camel.DefaultShsMessageToCamelProcessor;
import se.inera.axel.shs.processor.SimpleLabelValidator;

public class ShsCmdlineRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		configureSsl();

		onException(Exception.class)
		.handled(false)
		.log("${exception}");

		onException(HttpOperationFailedException.class)
		.handled(false)
		.log(LoggingLevel.ERROR, "Error message: ${exception.responseBody}")
		.log(LoggingLevel.ERROR, "Error code: ${exception.responseHeaders[X-shs-errorcode]}");


		from("direct:shsSendAsync")
		.bean(new DefaultCamelToShsMessageProcessor())
		.log("validating label...")
		.bean(SimpleLabelValidator.class)
		.log("sending message...")
		.to("{{shsServerUrl}}");


		from("direct:shsSendSync")
		.bean(new DefaultCamelToShsMessageProcessor())
		.log("validating label...")
		.bean(SimpleLabelValidator.class)
		.log("sending message...")
		.to("{{shsServerUrl}}")
		.bean(new DefaultShsMessageToCamelProcessor());
	}
	
	private void configureSsl() {
		SSLContextParameters sslContextParameters = getContext().getRegistry().lookup("mySslContext", SSLContextParameters.class);
		
		ProtocolSocketFactory factory =
			    new SSLContextParametersSecureProtocolSocketFactory(sslContextParameters);

		Protocol.registerProtocol("https",
				new Protocol(
						"https",
						factory,
						443));
	}
}