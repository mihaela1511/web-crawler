# web-crawler

Dupa pornirea aplicatiei va apare mesajul: "Introduceti comanda:"

Pentru a seta configuratia WebCrawler-ului:

crawler crawl <cale_absoluta_fisier_configurare>

Exemplu continut fisier de configurare:

n_threads=50

delay=100ms

root_dir=D:/Download

log_level=3

Pentru a descarca website-urile pe baza unui fisier ce contine o lista de URL-uri dat ca parametru:

crawler download <cale_absoluta_fisier_ce_contine_url>

Pentru a descarca website-urile in functie de o lista de tipuri de fisiere acceptate:

crawler list <tip_fisier_1> <tip_fisier_2> ... <tip_fisier_n>

crawler download <cale_absoluta_fisier_ce_contine_url>

Pentru a descarca doar fisierele din website-uri care nu depasesc dimensiunea maxima:

crawler maxDim <dimensiune_maxima_in_MB>

crawler download <cale_absoluta_fisier_ce_contine_url>

Pentru a parsa toate fisierele care contin unul sau mai multe cuvinte cheie dintr-un folder ce 
contine un website anterior descarcat:

crawler localSiteDirectory <cale_absoluta_folder_website_local>

crawler search <cuvant_cheie_1> <cuvant_cheie_2> ... <cuvant_cheie_n>

Fisierele extrase se vor gasi intr-un folder cu numele "KeywordFilteredFiles" in interiorul folderului
ce contine website-ul anterior descarcat dat ca parametru.

Pentru a parsa toate fisierele in functie de o lista de tipuri acceptate dintr-un folder ce 
contine un website anterior descarcat:

crawler localSiteDirectory <cale_absoluta_folder_website_local>

crawler list <tip_fisier_1> <tip_fisier_2> ... <tip_fisier_n>

Fisierele extrase se vor gasi intr-un folder cu numele "FileTypeFilteredFiles" in interiorul folderului
ce contine website-ul anterior descarcat dat ca parametru.

Pentru a parsa toate fisierele ce nu depasesc o dimensiune maxima dintr-un folder ce 
contine un website anterior descarcat:

crawler localSiteDirectory <cale_absoluta_folder_website_local>

crawler maxDim <dimensiune_maxima_in_MB>

Fisierele extrase se vor gasi intr-un folder cu numele "DimensionFilteredFiles" in interiorul folderului
ce contine website-ul anterior descarcat dat ca parametru.

Toate mesajele returnate de aplicatie vor fi salvate intr-un fisier de log numit "web-crawler.log"
ce va fi creat in directorul din care a fost rulata aplicatia. Tipurile de mesaje scrise in fisierul
de log se bazeaza pe nivelul de logare setat prin intermediul configuratiei WebCrawler-ului.
