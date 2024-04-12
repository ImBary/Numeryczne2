
#!/bin/bash

cd ~/Numeryczne2/algnum2/src && rm *.csv && echo "A1, A2, A3" > czasy.csv && touch gaus.csv && touch gausChoice.csv && touch gausSiedel.csv && cd ..

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

python3 createPlotOfTimes.py
python3 h1.py
mv *.jpg Graphs








