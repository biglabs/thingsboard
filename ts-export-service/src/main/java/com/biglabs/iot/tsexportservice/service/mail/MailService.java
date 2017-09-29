/**
 * Copyright © 2016-2017 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.biglabs.iot.tsexportservice.service.mail;

import com.biglabs.iot.tsexportservice.exception.TsExportException;
import com.fasterxml.jackson.databind.JsonNode;

public interface MailService {

    void updateMailConfiguration();

    void sendEmail(String email, String subject, String message) throws TsExportException;
    
    void sendTestMail(JsonNode config, String email) throws TsExportException;
    
}
