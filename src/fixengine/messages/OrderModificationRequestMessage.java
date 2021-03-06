/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fixengine.messages;

import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.HandlInst;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

/**
 * This class represents the Order Cancel/Replace Request (a.k.a. Order
 * Modification Request) message.
 * 
 * @author Pekka Enberg
 */
public class OrderModificationRequestMessage extends AbstractMessage implements RequestMessage, CancelRequestMessage {

    public OrderModificationRequestMessage() {
        this(new MessageHeader(MsgTypeValue.ORDER_MODIFICATION_REQUEST));
    }

    public OrderModificationRequestMessage(MessageHeader header) {
        super(header);

        field(OrigClOrdID.TAG);
        field(ClOrdID.TAG);
        field(HandlInst.TAG);
        field(Symbol.TAG);
        field(SecurityType.TAG, Required.NO);
        field(MaturityMonthYear.TAG, Required.NO);
        field(Side.TAG);
        field(TransactTime.TAG);
        field(OrderQty.TAG);
        field(OrdType.TAG);
        field(Currency.TAG, Required.NO);
        field(Price.TAG, Required.NO);
    }

    @Override public OrdTypeValue getOrdType() {
        return getEnum(OrdType.TAG);
    }

    @Override public SideValue getSide() {
        return getEnum(Side.TAG);
    }

    @Override public String getClOrdId() {
        return getString(ClOrdID.TAG);
    }

    @Override public double getOrderQty() {
        return getFloat(OrderQty.TAG);
    }

    @Override public String getOrigClOrdId() {
        return getString(OrigClOrdID.TAG);
    }

    @Override public String getSymbol() {
        return getString(Symbol.TAG);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
