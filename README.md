
#    POOTv
nume:Marin Iulia-Alina\
link to git repo: https://github.com/mariniulia/Proiect1_PooTv.git

# Implementari interesante

Clasa currentPlatform
- se ocupa cu situatia actuala a site ului, retine erorile
  curente, lista de filme curenta si userul curent. Este o clasa de tip singleton,
  deoarece exista o singura instanta la orice moment dat. Aceasta contine o alta
  instanta singleton anume Hierarchy.

Clasa Hierarchy
- este reprezentarea tip arbore a ierarhiei de pagini. Ierarhia
  este una singura ,universal valabila. In constructorul ei , cream paginile si le
  legam intre ele conform structurii date. Am considerat ca e cea mai buna abordare,
  structura fiind usor de extins si modificat, fara sa creez noi clase.
- Aici retinem si pagina curenta pe care ne aflam, fiind usor sa verificam in ce
  pagini avem acces in orice moment dat.


*Obs. De aici mi a fost usor sa modific posibilitatea mutatului pe o pagina pe care
sunt deja (lucru nespecificat in cerinta dar pe care l am descoperit spre finalul testelor).

# Functionalitate:
Clasa Main\
*Aici ma folosesc de scheletul primei teme pentru a lua inputul ca in prima tema\
creez o clasa de tip starter si pornesc pornesc programul.Dupa executia programlui \
suprapun outputul corespunzator cerintei,

Clasa Starter\
*Ma folosesc de o clasa Starter, care pune toata informatia din input in clasele
de input,apoi preiau informatia din aceste clase si o pun in clasele mele.

*Apoi itereaza prin toate actiunile primite si le porneste efectiv pe fiecare in parte
resetanf platforma conform actiunilor si erorilor respective.

*Tot aici pun si outputul in noduri prin functii care transforma user in nod si
movie in nod.

*Am lasat o singura clasa user, diferentele intre userul normal si cel premium
fiind minime, nu a fost nevoie sa fac mostenire pentru premium inca.

Clasa Actions\
*Aici in metoda start, pornirea actiunii, o impart in "change page" si "on page"\
2 metode care efectueaza actiunea conform tipului. \

-daca este de tipul "change page",verificam in arbore daca nodul curent,\
in care ne aflam, contine un copil cu numele paginii pe care vrem sa ne mutam\
cu alte cuvinte, daca ave, voie sa ne mutam pe pagina respectiva

-daca e de tipul "on page", verificam daca nodul curent, pagina pe care ne aflam \
contine feature-ul pe care vrem sa il folosim, cu alte cuvinte daca putem efectua\
actiunea din nodul in care ne alfam.

*Apoi pentru fiecare feture in functie de caz, punem sau nu output

*Exista cateva pregatiri speciale datorate movies si seeDetails efectuate prin\
metodele "prepareMoviesPage" si "prepareSeeDetailsPage", deoarece in functie de\
erori afisam sau nu lista de filme sau filmul selectat.

# Functionalitate:
Pattern observer:\
Am considerat fiecare user un observer care se poate abona prin subscribe la genuri de filme,\
pe care le am implementat drept clase extinse de la subject.\
Userii sunt notificati de adagarea filmelor din tipul respectiv.\
Pentru database delete, ne folosim de capacitatea userilor de a fi notificati. Construim \
notificarea si o trimitem la metoda update din cadrul userului,care o va adauga in coada de\
notificari a acestuia. Il compensam apoi pe user in functie de tipul contului.

Pattern builder:\
L am folosit pentru a construi action, fiind cea mai vasta clasa ,majoritatea putand fi nule

Pattern strategy:\
L-am folosit pentru a efectua tipurile diferite de actiuni (on page, change page, back,\
database) in functie de tipul lor, alegand la runtime algoritmul necesar fiecareia.\

Clasa recommandation

*Fiecare user are un camp recomandations, in care stocam un vector de filme recomandate \
si un database de tipul GenreDatabase

Clasa genreDatabase\
Aici stocam un vector de genreStatus numit top, si cele x-genuri de filme. 

Clasa genreStatus\
stocheaza numele genului si numarul de likeuri pe care le are de la userul in care ne aflam.

Gandim asa:

*Se stocheaza un database cu genurile, si topul lor pentru user.-> la fiecare like,actualizam\
baza de date,implicit recomandarile.

*In GenreDatabase se ordoneaza genurile dupa numarul de likeuri de la user,apoi lexicografic

*In clasa recommendation, luam filmele disponibile, le scoatem pe cele deja vizionate, le ordonam\
dupa numarul de likeuri, apoi le incarcam in vectorul recommendations, conform ordinei genurilor\
din vectorul top.

Am ales acest tip de implementare pentru ca:\
1.fiecare user are facute recomandarile in spate, insa cel standard nu le poate accesa.Astfel\
daca un user face trecerea de la standard la premium,poate vizualiza filmele recomandate in \
functie de tot istoricul ,nu doar de “acum incolo”

2.sortarea se face foarte simplu fara sa retinem intern alte lucruri

3.cand userul vede filmul recomandat, se trece automat la urmatorul, pentru ca le avem salvate\
in vectorul de recomandari, nu e doar o instanta ce trebuie calculata la fiecare modificare

4.aceasta structura se modifica doar cand apar actiuni ce ar modifica ordinea, nu se recalculeaza\
de fiecare data cand avem nevoie de ea.

Back

Pentru etapa 1 am considerat ca implementarea ierarhiei de pagini sub forma de arbore e cea mai
generica.\
Asta m a ajutat enorm de mult pentru aceasta bucata. Am facut o stiva ce retine paginile prin care\
am fost si pentru fiecare “change page” fac un push, iar pentru fiecare back,un pop.\
Aceasta stiva este retinuta in ierarhie,deoarece doar cand vreau sa modific pozitia curenta am\
nevoie de ea.\
Dupa care pur si simplu ma mut pe pagina de care am nevoie.

Obs: daca back ar fi functionat pe principiul “ne intoarcem pe pagina parinte”, in implementarea cu \
arbore, se rezolva in maxim 2 linii de cod.
