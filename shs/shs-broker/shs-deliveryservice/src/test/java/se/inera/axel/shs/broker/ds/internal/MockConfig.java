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
package se.inera.axel.shs.broker.ds.internal;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.inera.axel.shs.broker.messagestore.MessageLogService;
import se.inera.axel.shs.broker.messagestore.MessageState;
import se.inera.axel.shs.broker.messagestore.ShsMessageEntry;
import se.inera.axel.shs.broker.messagestore.ShsMessageEntryMaker;
import se.inera.axel.shs.mime.ShsMessage;
import se.inera.axel.shs.mime.ShsMessageMaker;
import se.inera.axel.shs.xml.label.ShsLabelMaker;
import se.inera.axel.shs.xml.label.TransferType;

import java.util.ArrayList;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static se.inera.axel.shs.mime.ShsMessageMaker.ShsMessageInstantiator.label;
import static se.inera.axel.shs.xml.label.ShsLabelMaker.*;
import static se.inera.axel.shs.xml.label.ShsLabelMaker.ShsLabelInstantiator.*;

/**
 * @author Björn Bength, bjorn.bength@r2m.se
 */
@Configuration
public class MockConfig {

    @Mock
    MessageLogService messageLogService;


    public MockConfig() {
        MockitoAnnotations.initMocks(this);
    }

    @Bean
    public MessageLogService messageLogService() {
        final List<ShsMessageEntry> entries = new ArrayList<ShsMessageEntry>();

        entries.add(make(a(ShsMessageEntryMaker.ShsMessageEntry,
                with(ShsMessageEntryMaker.ShsMessageEntry.state, MessageState.RECEIVED),
                with(ShsMessageEntryMaker.ShsMessageEntry.label, a(ShsLabel,
                        with(endRecipient, a(EndRecipient, with(EndRecipient.value,
                                ShsLabelMaker.DEFAULT_TEST_ENDRECIPIENT))),
                        with(transferType, TransferType.ASYNCH))))));

        entries.add(make(a(ShsMessageEntryMaker.ShsMessageEntry,
                with(ShsMessageEntryMaker.ShsMessageEntry.state, MessageState.RECEIVED),
                with(ShsMessageEntryMaker.ShsMessageEntry.label, a(ShsLabel,
                        with(to, a(To, with(To.value, ShsLabelMaker.DEFAULT_TEST_TO))),
                        with(transferType, TransferType.ASYNCH))))));

        entries.add(make(a(ShsMessageEntryMaker.ShsMessageEntry,
                with(ShsMessageEntryMaker.ShsMessageEntry.state, MessageState.RECEIVED),
                with(ShsMessageEntryMaker.ShsMessageEntry.label, a(ShsLabel,
                        with(to, a(To, with(To.value, ShsLabelMaker.DEFAULT_TEST_TO))),
                        with(transferType, TransferType.ASYNCH))))));


        given(messageLogService.listMessages(any(String.class), any(MessageLogService.Filter.class)))
                .willAnswer(new Answer<Iterable<ShsMessageEntry>>() {
                    @Override
                    public Iterable<ShsMessageEntry> answer(InvocationOnMock invocation) throws Throwable {
                        String shsTo = (String)invocation.getArguments()[0];
                        if (shsTo == null) {
                            throw new IllegalArgumentException("shsTo must be provided");
                        }

                        if (!shsTo.equals(ShsLabelMaker.DEFAULT_TEST_TO))
                            return new ArrayList<ShsMessageEntry>();

                        return entries;
                    }
                });

        given(messageLogService.findEntryByShsToAndTxid(any(String.class), any(String.class)))
        .willAnswer(new Answer<ShsMessageEntry>() {
            @Override
            public ShsMessageEntry answer(InvocationOnMock invocation) throws Throwable {

                System.out.println("0:" + (String)invocation.getArguments()[0]);
                System.out.println("1:" + (String)invocation.getArguments()[1]);
                for (ShsMessageEntry entry : entries) {
                    if (entry.getLabel().getTxId().equals((String)invocation.getArguments()[1])) {
                        System.out.println("RETURN!!!!!!!!:" + entry);
                        return entry;
                    }
                }

                return null;
            }
        });


        given(messageLogService.fetchMessage(any(ShsMessageEntry.class)))
        .willAnswer(new Answer<ShsMessage>() {
            @Override
            public ShsMessage answer(InvocationOnMock invocation) throws Throwable {

                for (ShsMessageEntry entry : entries) {
                    if (entry.getLabel().getTxId()
                            .equals(((ShsMessageEntry)invocation.getArguments()[0]).getLabel().getTxId())) {
                        return make(a(ShsMessageMaker.ShsMessage,
                                                with(label, entry.getLabel())));
                    }
                }

                return null;
            }
        });

        given(messageLogService.acknowledge(any(ShsMessageEntry.class)))
        .willAnswer(new Answer<ShsMessageEntry>() {
            @Override
            public ShsMessageEntry answer(InvocationOnMock invocation) throws Throwable {
                for (ShsMessageEntry entry : entries) {
                    if (entry.getLabel().getTxId()
                            .equals(((ShsMessageEntry)invocation.getArguments()[0]).getLabel().getTxId())) {

                        entry.setAcknowledged(true);
                        return entry;
                    }
                }

                return null;

            }
        });

        return messageLogService;
    }

}
