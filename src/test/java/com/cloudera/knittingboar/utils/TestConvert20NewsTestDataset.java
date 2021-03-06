/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.knittingboar.utils;

import java.io.IOException;

import junit.framework.TestCase;

public class TestConvert20NewsTestDataset extends TestCase {


  public void testNaiveBayesFormatConverter() throws IOException {
        
    int count = DatasetConverter.ConvertNewsgroupsFromSingleFiles("/Users/jpatterson/Downloads/datasets/20news-bydate/20news-bydate-train/", "/Users/jpatterson/Downloads/datasets/20news-kboar/train-dataset-unit-test/", 21000);
    
    //int count = DatasetConverter.ConvertNewsgroupsFromSingleFiles("/Users/jpatterson/Downloads/datasets/20news-bydate/20news-bydate-train/", "/Users/jpatterson/Downloads/datasets/20news-kboar/train4/", 2850);
    
    System.out.println( "Total: " + count );
    
    assertEquals( 11314, count );
    
  }  
  
  
}
