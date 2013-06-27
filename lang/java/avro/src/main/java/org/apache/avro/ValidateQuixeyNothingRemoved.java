/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.apache.avro;

import org.apache.avro.io.parsing.QuixeyResolvingGrammarGenerator;
import org.apache.avro.io.parsing.Symbol;

import java.io.IOException;

/**
 * A {@link org.apache.avro.SchemaValidationStrategy} that checks that the {@link org.apache.avro.Schema} to
 * validate can read the existing schema according to the default Avro schema
 * resolution rules.
 *
 */
class ValidateQuixeyNothingRemoved implements SchemaValidationStrategy {

  /**
   * Validate that the first schema has not removed any fields or modified
   * any fields, and existing schema can still read new schema.
   *
   * @throws org.apache.avro.SchemaValidationException
   *           if the first schema has removed fields from existing, and
   *           any added fields preserve Avro readability from existing schemas
   *           to read toValidate schemas.
   */
  @Override
  public void validate(Schema toValidate, Schema existing)
      throws SchemaValidationException {
    // check normal Avro restrictions on mutual readability
    //ValidateMutualRead.canRead(existing, toValidate);

    boolean error = false;
    try {
      error = Symbol.hasErrors(new QuixeyResolvingGrammarGenerator().generate(existing, toValidate));
    } catch (IOException e) {
      throw new SchemaValidationException(toValidate, existing, e);
    }

    if (error) {
      throw new SchemaValidationException(toValidate, existing);
    }
  }

}
