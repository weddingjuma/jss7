/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CancelRequestIndicationTest {

	public byte[] getData1() {
		return new byte[] { (byte) 128, 2, 42, (byte) 248 };
	}

	public byte[] getData2() {
		return new byte[] { (byte) 129, 0 };
	}

	public byte[] getData3() {
		return new byte[] { (byte) 162, 3, (byte) 129, 1, 20 };
	}
	
	@Test(groups = { "functional.decode","circuitSwitchedCall"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		CancelRequestIndicationImpl elem = new CancelRequestIndicationImpl();
		int tag = ais.readTag();
		assertEquals(tag, 0);
		elem.decodeAll(ais);
		assertEquals((int) elem.getInvokeID(), 11000);
		assertFalse(elem.getAllRequests());
		assertNull(elem.getCallSegmentToCancel());

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new CancelRequestIndicationImpl();
		tag = ais.readTag();
		assertEquals(tag, 1);
		elem.decodeAll(ais);
		assertNull(elem.getInvokeID());
		assertTrue(elem.getAllRequests());
		assertNull(elem.getCallSegmentToCancel());

		data = this.getData3();
		ais = new AsnInputStream(data);
		elem = new CancelRequestIndicationImpl();
		tag = ais.readTag();
		assertEquals(tag, 2);
		elem.decodeAll(ais);
		assertNull(elem.getInvokeID());
		assertFalse(elem.getAllRequests());
		assertNull(elem.getCallSegmentToCancel().getInvokeID());
		assertEquals((int)elem.getCallSegmentToCancel().getCallSegmentID(), 20);
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall"})
	public void testEncode() throws Exception {

		CancelRequestIndicationImpl elem = new CancelRequestIndicationImpl(11000);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		elem = new CancelRequestIndicationImpl(true);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

		CallSegmentToCancelImpl callSegmentToCancel = new CallSegmentToCancelImpl(null, 20);
		// Integer invokeID, Integer callSegmentID
		elem = new CancelRequestIndicationImpl(callSegmentToCancel);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
	}
}
