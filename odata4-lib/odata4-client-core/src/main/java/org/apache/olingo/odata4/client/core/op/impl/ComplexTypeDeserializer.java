/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.odata4.client.core.op.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.olingo.odata4.client.core.edm.AbstractComplexType;
import org.apache.olingo.odata4.client.core.edm.v4.AnnotationImpl;
import org.apache.olingo.odata4.commons.api.edm.constants.ODataServiceVersion;

public class ComplexTypeDeserializer extends AbstractEdmDeserializer<AbstractComplexType> {

  @Override
  protected AbstractComplexType doDeserialize(final JsonParser jp, final DeserializationContext ctxt)
          throws IOException, JsonProcessingException {

    final AbstractComplexType complexType = ODataServiceVersion.V30 == client.getServiceVersion()
            ? new org.apache.olingo.odata4.client.core.edm.v3.ComplexTypeImpl()
            : new org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl();

    for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
      final JsonToken token = jp.getCurrentToken();
      if (token == JsonToken.FIELD_NAME) {
        if ("Name".equals(jp.getCurrentName())) {
          complexType.setName(jp.nextTextValue());
        } else if ("Abstract".equals(jp.getCurrentName())) {
          ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                  setAbstractEntityType(BooleanUtils.toBoolean(jp.nextTextValue()));
        } else if ("BaseType".equals(jp.getCurrentName())) {
          ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                  setBaseType(jp.nextTextValue());
        } else if ("OpenType".equals(jp.getCurrentName())) {
          ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                  setOpenType(BooleanUtils.toBoolean(jp.nextTextValue()));
        } else if ("Property".equals(jp.getCurrentName())) {
          jp.nextToken();
          if (complexType instanceof org.apache.olingo.odata4.client.core.edm.v3.ComplexTypeImpl) {
            ((org.apache.olingo.odata4.client.core.edm.v3.ComplexTypeImpl) complexType).
                    getProperties().add(jp.readValueAs(
                                    org.apache.olingo.odata4.client.core.edm.v3.PropertyImpl.class));
          } else {
            ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                    getProperties().add(jp.readValueAs(
                                    org.apache.olingo.odata4.client.core.edm.v4.PropertyImpl.class));
          }
        } else if ("NavigationProperty".equals(jp.getCurrentName())) {
          jp.nextToken();
          ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                  getNavigationProperties().add(jp.readValueAs(
                                  org.apache.olingo.odata4.client.core.edm.v4.NavigationPropertyImpl.class));
        } else if ("Annotation".equals(jp.getCurrentName())) {
          jp.nextToken();
          ((org.apache.olingo.odata4.client.core.edm.v4.ComplexTypeImpl) complexType).
                  setAnnotation(jp.readValueAs(AnnotationImpl.class));
        }
      }
    }

    return complexType;
  }
}