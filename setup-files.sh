#!/bin/bash
for i in $(seq -w 01 25); do
   mkdir "./src/main/java/day$i"
   touch "./src/main/resources/day${i}.txt"
   touch "./src/main/resources/day${i}-sample.txt"

   cp ./src/main/java/template/Template.java "./src/main/java/day${i}/Day${i}.java"
   sed -i '' "s/Template/Day${i}/g" "src/main/java/day${i}/Day${i}.java"
   sed -i '' "s/template/day${i}/g" "src/main/java/day${i}/Day${i}.java"
done

echo "Setup done."
