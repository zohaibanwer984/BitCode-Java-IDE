@ECHO ON
ECHO "HI"
TITLE BitCode Running
CD %2
java -cp %2 %1
PAUSE
EXIT