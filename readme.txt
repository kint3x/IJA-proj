
Projekt StorageApp, využíva Java SE 8.

AUTORI:

    Košina Simon, xkosin09
    Matějka Martin, xmatej55

POPIS:

    Aplikácia predstavujúca sklad tovaru. V sklade sa nachádzajú regále s položkami.

KONFIGURÁCIA:

    Konfigurácia skladu je uložená v JSON súboroch viz. priečinok 'data'. Zatiaľ sú mená
    načítavaných súborov zadané na pevno, no bude možné si priamo vybrať kofiguračnný
    súbor v aplikácii.

KONFIGURÁCIA REGÁLOV:

    Objekt obsahuje následujúce atribúty, viz. 'data/shelfs.json':

        width   - šírka skladu
        height  - výška skladu
        shelfs  - zoznam objektov reprezentujúci regále, ktoré ležia na políčkach v tvare obdlížnika,
                  definovaného bodmi [x1, y1] a [x2, y2]

    Regále umiestnené mimo hranice skladu budú pri vytváraní vynechané.

KONFIGÚRACIA POLOŽIEK:

    Objekt má jeden atribút 'items', ktorý je zoznamom objektov reprezentujúcich jednotlivé položky.
    Dané objekty majú následujúce atribúty, viz. 'data/items.json':

        name    - názov položky
        count   - počet kusov
        x       - x-ová súradnica regálu, kde bude umiestnená položka
        y       - y-ová súradnica regálu, kde bude umiestnená položka

    V bode [x,y] musí byť predom vytvorený regál, inač bude daná položka vynechaná.