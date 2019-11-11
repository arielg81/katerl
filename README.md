# katerl
Erlang External Term Format for kafkatool

Plugin for kafkatool2(2.0.4) to decode `term_to_binary` messages. It is an alfa version, but it is usefull for us, maybe it will be useful for someone else. 

## compile
```
mkdir out
javac -d out -classpath OtpErlang.jar -sourcepath src src/com/kafkatool/external/ErlBinaryTermDecorator.java
cd out
jar cvf katerl.jar .
```

## install

1. Copy OtpErlang.jar in kafkatool2/lib
2. Edit kafkatool2/kafkatool file, add after the last "add_class_path"
   
   add_class_path "$app_home/lib/OtpErlang.jar"

3. Copy katerl.jar kafkatool2/plugins/
