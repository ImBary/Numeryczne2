
#!/bin/bash
for i in {1..5}
do
    python3 createTest.py
    python3 parkDrawer.py
    javac src/Main.java

done






