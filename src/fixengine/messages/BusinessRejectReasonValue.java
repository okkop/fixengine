/*
 * Copyright 2009 the original author or authors.
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

/**
 * @author Pekka Enberg 
 */
public enum BusinessRejectReasonValue implements Formattable {
    /** Other.  */
    OTHER(0),
    
    /** Unknown ID.  */
    UNKNOWN_ID(1),

    /** Unknown Security.  */
    UNKNOWN_SECURITY(2),

    /** Unknown Message Type.  */
    UNKNOWN_MESSAGE_TYPE(3),

    /** Application not available.  */
    APPLICATION_NOT_AVAILABLE(4),

    /** Conditionally Required Field Missing.  */
    CONDITIONALLY_REQUIRED_FIELD_MISSING(5),

    /** Not authorized.  */
    NOT_AUTHORIZED(6),

    /** DeliverTo firm not available at this time.  */
    DELIVER_TO_FIRM_NOT_AVAILABLE(7);

    private final int value;

    BusinessRejectReasonValue(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static BusinessRejectReasonValue parse(int value) {
        for (BusinessRejectReasonValue type : BusinessRejectReasonValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}
