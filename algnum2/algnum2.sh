
#!/bin/bash

cd src && rm *.csv && echo "A1, A2, A3" > czasy.csv && touch gaus.csv && touch gausChoice.csv && touch gausSiedel.csv && cd ..

for i in {1..9}
do
    export num=$i
    python3 createTest.py
    python3 parkDrawer.py
    cd src
    pwd
    javac Main.java
    java Main.java
    cd ..
    pwd

done

python3 createPlotOfTimes.py
python3 h1.py
mv *.jpg Graphs
cd Graphs


echo "\\documentclass{article}" > sprawozdanie.tex
echo "\\usepackage{graphicx}" >> sprawozdanie.tex
echo "\\title{Sprawozdanie}" >> sprawozdanie.tex
echo "\\author{Michal Safuryn, Bartosz Smolibowski, Wojciech Balcer}" >> sprawozdanie.tex
echo "\\date{\\today}" >> sprawozdanie.tex
echo "\\begin{document}" >> sprawozdanie.tex
echo "\\maketitle" >> sprawozdanie.tex
echo "\\section{Parki}" >> sprawozdanie.tex
for z in {1..9}
do
    echo "\\begin{figure}[h!]" >> sprawozdanie.tex
    echo "\\centering" >> sprawozdanie.tex
    echo "\\includegraphics[width=0.75\\textwidth]{${z}.jpg}" >> sprawozdanie.tex
    echo "\\caption{Park numer ${z}}" >> sprawozdanie.tex
    echo "\\end{figure}" >> sprawozdanie.tex
done
echo "Projekt zawiera rozszerzenia R0 i R1. Czyli moze sie znalezc pare wyjsc oraz pare osk. Pisany w javie, pythonie i bashu. Korzystamy ze struktury danych DS1. Wszystkie macierze sa zapamietywane jako mapa map i intow z wartosciami nierowynymi 0. " >> sprawozdanie.tex

echo "\\section{Hipoteza 1}" >> sprawozdanie.tex
echo "H1: Algorytm A2 zwykle daje dokladniejsze wyniki niz A1. Roznica dokladnosci rosnie wraz z rozmiarem macierzy i liczba niezerowych wspolczynnikow." >> sprawozdanie.tex
for x in {1..9}
do
    echo "\\begin{figure}[h!]" >> sprawozdanie.tex
    echo "\\centering" >> sprawozdanie.tex
    echo "\\includegraphics[width=0.75\\textwidth]{${x}.jpg}" >> sprawozdanie.tex
    echo "\\caption{Park numer ${x}}" >> sprawozdanie.tex
    echo "\\end{figure}" >> sprawozdanie.tex
done
echo "Hipoteza nieprawdziwa. Dla kazdego przykladu wyniki otrzymywane byly takie same." >> sprawozdanie.tex

echo "\\section{Hipoteza 2}" >> sprawozdanie.tex
echo "H2: Algorytm A3 dziala dla postawionego zadania" >> sprawozdanie.tex
echo "???" >> sprawozdanie.tex

echo "\\section{Hipoteza 3}" >> sprawozdanie.tex
echo "H3: Jesli algorytm A3 jest zbiezny do rozwiazania, to wyniki otrzymujemy istotnie szybciej niz dla A1 i A2" >> sprawozdanie.tex

echo "Hipoteza (nie)prawdziwa" >> sprawozdanie.tex
for y in {1..9}
do
    echo "\\begin{figure}[h!]" >> sprawozdanie.tex
    echo "\\centering" >> sprawozdanie.tex
    echo "\\includegraphics[width=0.75\\textwidth]{${y}.jpg}" >> sprawozdanie.tex
    echo "\\caption{Park numer ${y}}" >> sprawozdanie.tex
    echo "\\end{figure}" >> sprawozdanie.tex
done
echo "\\end{document}" >> sprawozdanie.tex


pdflatex sprawozdanie.tex






