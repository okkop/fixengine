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

import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExDestination;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRestatementReason;
import fixengine.tags.ExecTransType;
import fixengine.tags.ExecType;
import fixengine.tags.LastMkt;
import fixengine.tags.LastPx;
import fixengine.tags.LastQty;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdRejReason;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;

/**
 * @author Pekka Enberg 
 */
public class ExecutionReportMessage extends AbstractMessage {

    public ExecutionReportMessage() {
        this(new MessageHeader(MsgTypeValue.EXECUTION_REPORT));
    }

    public ExecutionReportMessage(MessageHeader header) {
        super(header);

        field(OrderID.TAG);
        field(ClOrdID.TAG, Required.NO);
        field(OrigClOrdID.TAG, Required.NO);
        field(ExecID.TAG);
        field(ExecTransType.TAG);
        field(ExecType.TAG);
        field(OrdStatus.TAG);
        field(OrdRejReason.TAG, Required.NO);
        field(ExecRestatementReason.TAG, Required.NO);
        field(Symbol.TAG);
        field(SecurityType.TAG, Required.NO);
        field(MaturityMonthYear.TAG, Required.NO);
        field(Side.TAG);
        field(OrderQty.TAG);
        field(LastQty.TAG, Required.NO);
        field(LastPx.TAG, Required.NO);
        field(LeavesQty.TAG);
        field(OrdType.TAG, Required.NO);
        field(Price.TAG, new Required() {
            @Override public boolean isRequired() {
                return OrdTypeValue.LIMIT.equals(getEnum(OrdType.TAG));
            }
        });
        field(TimeInForce.TAG, Required.NO);
        field(CumQty.TAG);
        field(AvgPx.TAG);
        field(TransactTime.TAG, Required.NO);
        field(Text.TAG, Required.NO);
        field(ExDestination.TAG, Required.NO);
        field(LastMkt.TAG, Required.NO);
        field(Currency.TAG, Required.NO);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
