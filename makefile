#!/usr/bin/env make

JAR=/usr/bin/jar
JAVAC=/usr/bin/javac

RM=/usr/bin/rm
RMFLAGS=-f

SOURCES=$(wildcard *.java)
CLASSES=$(SOURCES:.java=.class)

all:	aoc.jar

debug:	aoc.jar
	java -jar aoc.jar --today --debug

today:	aoc.jar
	java -jar aoc.jar --today

aoc.jar:	$(CLASSES)
	$(JAR) cmvf META-INF/MANIFEST.MF aoc.jar `find . -name '*.class'`

clean:
	$(RM) $(RMFLAGS) `find . -name '*.class'`

proper:	clean
	$(RM) $(RMFLAGS) aoc.jar

%.class:	%.java
	$(JAVAC) $<
