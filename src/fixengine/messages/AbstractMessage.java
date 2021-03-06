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

import java.nio.ByteBuffer;

import lang.Objects;

import org.joda.time.DateTime;

import fixengine.Config;
import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.CheckSum;
import fixengine.tags.DeliverToCompID;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.OnBehalfOfCompID;
import fixengine.tags.OrigSendingTime;
import fixengine.tags.PossDupFlag;
import fixengine.tags.PossResend;
import fixengine.tags.SenderCompID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractMessage extends FieldContainer implements Message {
    private final MessageHeader header;

    protected AbstractMessage(MsgTypeValue msgType) {
        this(new MessageHeader(msgType));
    }

    protected AbstractMessage(MessageHeader header) {
        this.header = header;
    }

    public abstract void apply(MessageVisitor visitor);

    public void setHeaderConfig(Config config) {
        setBeginString(config.getVersion().value());
        setSenderCompId(config.getSenderCompId());
        setTargetCompId(config.getTargetCompId());
    }

    public String getMsgType() {
        return header.getMsgType();
    }

    public void setBeginString(String beginString) {
        header.setBeginString(beginString);
    }

    public String getBeginString() {
        return header.getBeginString();
    }

    public void setSenderCompId(String senderCompId) {
        header.setString(SenderCompID.TAG, senderCompId);
    }

    public String getSenderCompId() {
        return header.getString(SenderCompID.TAG);
    }

    public void setTargetCompId(String targetCompId) {
        header.setString(TargetCompID.TAG, targetCompId);
    }

    public String getTargetCompId() {
        return header.getString(TargetCompID.TAG);
    }

    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        header.setString(OnBehalfOfCompID.TAG, onBehalfOfCompId);
    }

    public void setDeliverToCompId(String deliverToCompId) {
        header.setString(DeliverToCompID.TAG, deliverToCompId);
    }

    public void setMsgSeqNum(int msgSeqNum) {
        header.setInteger(MsgSeqNum.TAG, msgSeqNum);
    }

    public int getMsgSeqNum() {
        return header.getInteger(MsgSeqNum.TAG);
    }

    public void setSendingTime(DateTime sendingTime) {
        header.setDateTime(SendingTime.TAG, sendingTime);
    }

    public DateTime getSendingTime() {
        return header.getDateTime(SendingTime.TAG);
    }

    public void setOrigSendingTime(DateTime origSendingTime) {
        header.setDateTime(OrigSendingTime.TAG, origSendingTime);
    }

    public DateTime getOrigSendingTime() {
        return header.getDateTime(OrigSendingTime.TAG);
    }

    public boolean hasOrigSendingTime() {
        return header.hasValue(OrigSendingTime.TAG);
    }

    public void setPossDupFlag(boolean possDupFlag) {
        header.setBoolean(PossDupFlag.TAG, possDupFlag);
    }

    public boolean getPossDupFlag() {
        return header.getBoolean(PossDupFlag.TAG);
    }

    public boolean getPossResend() {
        return header.getBoolean(PossResend.TAG);
    }

    public boolean hasValidTargetCompId(Config config) {
        return config.getSenderCompId().equals(getTargetCompId());
    }

    public boolean isPointToPoint() {
        return header.isPointToPoint();
    }

    @Override
    public boolean hasValidBeginString(Config config) {
        return config.getVersion().value().equals(getBeginString());
    }

    @Override
    public boolean hasValidSenderCompId(Config config) {
        return config.getTargetCompId().equals(getSenderCompId());
    }

    @Override
    public boolean isTooLowSeqNum(int seqNo) {
        return !getPossDupFlag() && getMsgSeqNum() < seqNo;
    }

    @Override
    public boolean hasAccurateSendingTime(DateTime currentTime) {
        return header.hasAccurateSendingTime(currentTime);
    }

    @Override
    public boolean hasOrigSendTimeAfterSendingTime() {
        return header.hasOrigSendTimeAfterSendingTime();
    }

    public void parse(ByteBuffer b) {
        super.parse(b);
        finishParse(b);
    }

    private void finishParse(ByteBuffer b) {
        if (b.hasRemaining()) {
            int tag = Tag.parseTag(b);
            Field field = header.lookup(tag);
            if (CheckSum.TAG.value() == tag) {
                throw new OutOfOrderTagException("CheckSum(10): Out of order tag");
            } else if (field != null) {
                throw new OutOfOrderTagException(field.prettyName() + ": Out of order tag");
            }
            throw new InvalidTagException("Tag not defined for this message: " + tag);
        }
    }

    public String format() {
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new StringField(MsgType.TAG, header.getMsgType()));
        buffer.append(header.format());
        buffer.append(super.format());
        buffer.prefix(new IntegerField(BodyLength.TAG, buffer.length()));
        buffer.prefix(new StringField(BeginString.TAG, header.getBeginString()));
        buffer.append(new StringField(CheckSum.TAG, CheckSum.format(buffer.checksum())));
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(new StringField(BeginString.TAG, header.getBeginString()).toString() + " ");
        result.append(new StringField(MsgType.TAG, header.getMsgType()).toString() + " ");
        for (Field field : this) {
            result.append(field.toString() + " ");
        }
        return result.toString();
    }
}
