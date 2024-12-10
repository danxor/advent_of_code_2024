#!/usr/bin/env make

JAR=/usr/bin/jar
JAVAC=/usr/bin/javac

RM=/usr/bin/rm
RMFLAGS=-f

SOURCES=$(wildcard *.java)
CLASSES=$(SOURCES:.java=.class)

all:	aoc.jar
	java -jar aoc.jar --today

aoc.jar:	$(CLASSES)
	$(JAR) cmvf META-INF/MANIFEST.MF aoc.jar *.class

clean:
	$(RM) $(RMFLAGS) *.class

proper:	clean
	$(RM) $(RMFLAGS) aoc.jar

%.class:	%.java
	$(JAVAC) $<
