#!/bin/bash

mkdir -p classes
javac -d classes -sourcepath ./src src/com/paymo/VerifyTransaction.java src/com/paymo/Graph.java
java -classpath classes com.paymo.VerifyTransaction ./paymo_input/batch_payment.txt ./paymo_input/stream_payment.txt ./paymo_output/output1.txt ./paymo_output/output2.txt ./paymo_output/output3.txt