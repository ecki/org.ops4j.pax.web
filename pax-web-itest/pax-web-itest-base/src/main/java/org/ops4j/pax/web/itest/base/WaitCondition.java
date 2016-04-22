/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.itest.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marc Klinger - mklinger[at]nightlabs[dot]de
 */
public abstract class WaitCondition {
	private static final long WAIT_TIMEOUT_MILLIS = 10000;
	private static final Logger LOG = LoggerFactory.getLogger(WaitCondition.class);


	private String description;
	private Long waitMillis;
	private Long sleep;

	protected WaitCondition(String description) {
		this.description = description;
	}

	protected WaitCondition(String description, long waitMillis) {
		this(description);
		this.waitMillis = waitMillis;
	}

	protected WaitCondition(String description, long waitMillis, long sleep) {
		this(description, waitMillis);
		this.sleep = sleep;
	}

	protected String getDescription() {
		return description;
	}

	protected abstract boolean isFulfilled() throws Exception;

	public void waitForCondition() throws InterruptedException {
		//CHECKSTYLE:OFF
		long startTime = System.currentTimeMillis();
		try {
			while (!isFulfilled() && System.currentTimeMillis() < startTime + (waitMillis != null ? waitMillis : WAIT_TIMEOUT_MILLIS)) {
				Thread.sleep(sleep != null ? sleep : 200);
			}
			if (!isFulfilled()) {
				LOG.warn("Waited for {} for {} ms but condition is still not fulfilled", getDescription(), System.currentTimeMillis() - startTime);
			} else {
				LOG.info("Successfully waited for {} for {} ms", getDescription(), System.currentTimeMillis() - startTime);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for " + getDescription(), e);
		}
		//CHECKSTYLE:ON
	}
}