/ Remember that keys are surrounded by brackets 
author:([author:`king`hemingway`flaubert`lazere`shasha] 
 address:`maine`expat`france`mamaroneck`newyork; 
 area: `horror`suffering`psychology`journalism`puzzles)

book:([book:`forwhomthebelltolls`oldmanandthesea`shining`secretwindow`clouds`madambovary`salambo`outoftheirminds]
 language: `english`english`english`english`english`french`french`english; numprintings: 3 5 4 2 2 8 9 2) 
 
/ Here we indicate that the author field is a foreign key to the table 
/ author and book to the table book. 
/ If we wished, we could also surround the two fields author and book 
/ by brackets to indicate that they are keys. 
bookauthor:([]
 author:`author$`hemingway`hemingway`king`king`king`flaubert`flaubert`shasha`lazere;
 book:`book$`forwhomthebelltolls`oldmanandthesea`shining`secretwindow`clouds`madambovary`salambo`outoftheirminds`outoftheirminds;
 numfansinmillions: 20 20 50 50 30 60 30 0.02 0.02) 
