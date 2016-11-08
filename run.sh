#!/usr/bin/env bash

javac ./src/VerifyTransaction.java ./src/Graph.java
java -classpath ./src VerifyTransaction ./paymo_input/batch_payment.txt ./paymo_input/stream_payment.txt ./paymo_output/output1.txt ./paymo_output/output2.txt ./paymo_output/output3.txt