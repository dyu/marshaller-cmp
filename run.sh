#!/bin/sh

[ -n "$RECOMPILE" ] && mvn -o -Pjwd -Djwd=compile compile

[ ! -n "$TYPE" ] && TYPE=protostuff

mkdir -p target

java -jar modules/$TYPE-marshaller/target/$TYPE-marshaller.jar -o target/$TYPE.txt $@


