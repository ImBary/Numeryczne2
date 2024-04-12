
#!/bin/bash
for i in {1..5}
do
    
    python3 createTest.py
    python3 parkDrawer.py
    cd ~/Numeryczne2/algnum2/src
    pwd
    javac Main.java
    java Main.java
    cd ..
    pwd

done






