
Am implementat simularea robotului pe harta, cat si a tuturor obiectelor ce o populeaza pe aceasta.

Mi-am organizat codul astfel:
## Pachete principale:
### 1 - entities: 
include toate entitatile ce pot fi prezente pe harta, continand totodata clasa parinte Entity, mostenita de toate clasele principale de entitati, care are setteri si getteri.
* **animals**: include clasele Animalelor. Clasa Animal este clasa parinte in acest pachet, implementand atat algoritmul
 de alegere a miscarii animalului/pentru mancat de animal (mancatul in sine de animal facandu-se in simulation), mancatul de resurse,
 si a altor metode necesare. Clasele Carnivore, Detritivore, Herbivore, Omnivore si Parasite mostenesc Animal si suprascriu metode pt posibilitati si eventual, pt verificare tip animal.
Totodata, enum-ul AnimalState cuprinde starile in care poate fi animalul
* **plants**: include clasele plantelor. Logica principala este in Plant, care are metode pt
 cresterea maturitatii plantei, respectiv cresterea nivelului de oxigen al plantei. Clasele Alga, Fern, FloweringPlant, GymnospersPlant si Moss mostenesc Plant si suprascriu doar metodele de getType,
 fiind insa instantiate cu probabilitatile/nivelurile de oxigen lor specifice.
* **resources**:
   * **air**: include clasele aerului. Logica principala este in Air, avand metode pt toxicitatea aerului, setari nivel oxigen,
 aflarea tip de calitate aer. Clasele care o mostenesc, DesertAir, MountainAir, PolarAir, TemperateAir, TropicalAir, implementeaza mai multe metode abstracte din clasa parinte,
calculand atat calitatea aerului specifica, cat si gettere pt campul extra al fiecareia. Totodata, DesertAir suprascrie o metoda goala { } din Air, necesara pt afisarea campului desertStorm.
   * **soils**: include clasele solului. Logica principala este in Soil, ce are metode similare(tip calitate sol, adaugare organic matter, etc.). Clasele copil, DesertSoil,
ForestSoil, GrasslandSoil, SwampSoil si TundraSoil ofera implementari metodelor abstracte, care sunt, de asemenea, similare celor de la aer (calc. calitate, camp extra, tip sol, posibilitati specifice)
   * **waters**: include clasele apei. Logica principala este in Water, avand metode pt calcul calitate, cat si bautul apei. Clasele copil, Lake, Pond, River, au rolul de a implementa o metoda ce returneaza tipul fiecareia.
### 2 - main: 
contine clasa Main. este entry-point-ul, intializeaza incarcarea datelor din input si declanseaza simularea prin apelarea metodei runCommand din Simulation.
### 3 - robot: 
contine Terrabot si Topic.
* **Terrabot** este clasa robotului, avand metode pt manipularea nivelului bateriei, gestionarea inventarului si a bazei de cunostiinte, 
* **Topic** este clasa ajutatoare pt stocarea informatiilor pe care le invata robotul, acele facts-uri.
### 4- simulation: centrul logicii in proiectul meu.
* **Cell**: defineste celula, avand getteri/setteri/metode de adaugare/stergere pt diferitele entitati care o pot popula.
* **Map**: defineste harta, ca o matrice de Cell, avand metode pt verificare coordonate, dar si pentru returnarea vecinilor unei celule, in ordinea ceruta in enunt.
* **EntityCreator**: are rolul de a instantia entitatile, pe baza inputului
* **Simulation**: baza logicii programului, aici fiind implementate toate comenzile de la input, cat si o sumedenie de functii ajutatoare pt implementarea acestora.
  * startSimulation - porneste simularea prin parsarea dimensiunilor din input si instantierea celulor si robotului
  * fillMap - umple harta folosindu-se de EntityCreator
  * runCommand - selecteaza ce comanda trebuie rulata, folosind un switch.
  * printStartSimulation - baga in nodul de afisare mesajele comenzii startSimulation.
  * runEnvironmentUpdates - aici se apeleaza updatarea mediului in functie de timestamp, aceasta metoda fiind apelata in foarte multe functii din Simulation.
  * printEndSimulation - afisare sfarsit simulare
  * printMap - afisare harta, coordonate, nr obiecte, calitati aer sol
  * printEnvConditions - printare conditii mediu, pt respectiva celula.
  * qualityScore - calculare calcul scor de calitate, folosind formula din enunt, folosita pentru miscarea robotului.
  * moveRobot - miscare robot, bazata pe scor si vecini
  * getEnergyStatus - afisare nivel baterie robot
  * rechargeBattery - crestere nivel baterie robot
  * changeWeatherConditions - schimbare vreme, bazata pe metodele din clasele copil de la entitati, verifica daca s-a schimbat si afiseaza raspunsul.
  * updateEnvironment - actualizeaza mediul, verifica daca entitatile au fost scanate de robot, apoi le aplica actualizare, totodata, la animal fiind aici scrisa implementarea logicii pentru mancarea animalului de animal,
                          intrucat scrierea ei in Animal ar fi incurcat conditia ca 1 cell contine un singur animal.
  * scanObject - scaneaza obiect, adauga in inventar.
  * learnFact - adauga fact in knowledge base
  * printKnowledgeBase - afiseaza knowledgebase, cu topic si facts
  * improveEnvironment - imbunatateste mediul prin inputurile specifice, aplicand la fiecare modificare in functie de caz.
Majoritatea functiilor din Simulation contin conditii foarte similare de eroare, modificate de la caz la caz, ex. check consum baterie etc. Totodata, ruleaza si comanda de aplicare a modificarilor mediului.

 