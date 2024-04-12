
#!/bin/bash
for i in {1..9}
do
    export num=$i
    python3 createTest.py
    python3 parkDrawer.py
    cd ~/Numeryczne2/algnum2/src
    pwd
    javac Main.java
    java Main.java
    cd ..
    pwd

done






