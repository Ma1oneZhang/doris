// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
import org.junit.Assert;
suite("test_cte_with_duplicate_consumer") {
    try {
        sql """
            WITH cte1(col1) AS (SELECT 1), cte2(col2_1, col2_2) AS (SELECT col1, col1 FROM cte1) SELECT * FROM cte2
        """
    } catch (Exception e) {
        // Duplicated inline view column alias: 'col1' in inline view: 'cte2''
        assertTrue(e.message.contains(" Duplicated inline view column alias"))
    }

    test {
        sql """
            WITH cte1(col1) AS (SELECT 1), cte2(col2_1) AS (SELECT col1 FROM cte1) SELECT * FROM cte2
        """

        result([[1]])
    }
}
