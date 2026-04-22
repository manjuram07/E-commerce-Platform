drop schema  if exists ekart_product ;

create schema ekart_product;
use `ekart_product`;

CREATE TABLE EK_PRODUCT(
	PRODUCT_ID INT auto_increment,
	NAME VARCHAR(500) NOT NULL,
	DESCRIPTION VARCHAR(1000) NOT NULL,
	CATEGORY VARCHAR(200) NOT NULL,
	BRAND VARCHAR(250) NOT NULL,
	PRICE BIGINT NOT NULL,
	QUANTITY SMALLINT NOT NULL,
	constraint EK_PRODUCT_ID_PK primary key ( PRODUCT_ID )
);



INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1000,'Bot E5s Plus','Smart phone with (13+13)MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor', 'Electronics - Mobile', 'Motobot', 16000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1001,'Xpress','Smart phone with (13+13)MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor', 'Electronics - Mobile', 'Motobot', 16000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1002,'Xpress Plus','Smart phone with 18MP rear camera and 16MP front camera, 4GB RAM and 32GB ROM,5 inch FHD display, MediTek T1009 processor', 'Electronics - Mobile', 'Motobot', 15000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1003,'Dzire','Smart phone with 16MP rear camera and 8MP front camera, 2GB RAM and 16GB ROM,5 inch FHD display, Hi-Tech processor', 'Electronics - Mobile', 'SangSung', 12000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1004,'Dzire Plus','Smart phone with 16MP rear camera and 8MP front camera, 6GB RAM and 32GB ROM,5.5 inch FHD display, Hi-Tech processor', 'Electronics - Mobile', 'SangSung', 15000,  100);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1005,'Xpress Music-360','Smart phone with 16MP rear camera and 8MP front camera, 2GB RAM and 16GB ROM,5 inch FHD display, DA sound', 'Electronics - Mobile', 'Technotron', 10000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1006,'CamBot','Basic phone with 26MP rear camera and 16MP front camera, 2GB RAM and 16GB ROM,4.5 inch HD display, OxiTech Processor', 'Electronics - Mobile', 'Technotron', 5000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1007,'Cambot Plus','Smart phone with 26MP rear camera and 16MP front camera, 4GB RAM and 32GB ROM,4.5 inch HD display, OxiTech Processor', 'Electronics - Mobile', 'Technotron', 8000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1008,'Impulse S','Smart phone with 16MP rear camera and 8MP front camera, 4GB RAM and 32GB ROM,5.5 inch FHD display, OxiTech Processor', 'Electronics - Mobile', 'Micron', 18000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1009,'Impulse S Plus','Smart phone with 18MP rear camera and 16MP front camera, 4GB RAM and 32GB ROM,5.5 inch FHD display, OxiTech Processor', 'Electronics - Mobile', 'Micron', 22000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1010,'Frost LP 6677','14-inch FHD display, Core-i3 5th Gn processor, 4GB RAM, 500GB HDD', 'Electronics - Laptop', 'CorpTech', 28000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1011,'Inspire LP N5577','15.6-inch UHD display, Core-i5 8th Gn processor, 8GB RAM, 1TB HDD, Finger Print Unlock ', 'Electronics - Laptop', 'CorpTech', 59000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1012,'NoteBook LP 8800','14-inch FHD display, Core-i3 7th Gen processor, 4GB RAM, 500GB HDD', 'Electronics - Laptop', 'Itronics', 38000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1013,'AWare LP 3636','17-inch UHD display,Core i7-7th Gen, 16GB RAm and 2TB HHDD', 'Electronics - Laptop', 'AW', 75000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1014,'Longitude LP E7440','15-inch FHD display, Core-i5 7th Gen processor, 8GB RAM, 750GB HDD', 'Electronics - Laptop', 'Lap+', 50000, 450);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1015,'ThinkStation LP 7000','14-inch FHD display, Core-2 duo processor, 4GB RAM, 500GB HDD', 'Electronics - Laptop', 'Thinkpad', 35000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1016,'IdeaStation LP 8090','14-inch FHD display, Core-i3 5th Gen processor, 4GB RAM, 750GB HDD', 'Electronics - Laptop', 'Ideapad', 50000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1017,'MaxBook Base','14-inch FHD display, I+ processor, 4GB RAM, 500GB HDD', 'Electronics - Laptop', 'Max+', 50000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1018,'MaxBook Air','14-inch FHD display, I++ processor, 4GB RAM, 650GB HDD', 'Electronics - Laptop', 'Max+', 70000, 250);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1019,'MaxBook Pro','17-inch UHD display, I++ duo processor, 16GB RAM, 500GB SSD', 'Electronics - Laptop', 'Max+', 155000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1020,'Corsair DSKTP 1919','All In One Desktop C2000(F0BB00YDIN,CPU J3060/4GB/1TB/19.5"LED/Dos)', 'Electronics - Desktop', 'Corsair', 25000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1021,'Corsair DSKTP 2020 PRO','20-c103in 19.45-inch All-in-One Desktop (Celeron J3060/4GB/500GB/Windows 10/Integrated Graphics), Black', 'Electronics - Desktop', 'Corsair', 35000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1022,'Precision DSKTP 2323','Pentium Quad Core/4 GB DDR3/1 TB/Free DOS - Black, 19.5 Inch Screen', 'Electronics - Desktop', 'Precision', 45000, 15);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1023,'Neuron DSKTP','Core i3-6th Gen/4 GB DDR 4/1 TB/Windows 10 Home-Black, 34.2 cm x 48.6 cm x 4.8 cm, 3.5 kg, 19.5 Inch Screen', 'Electronics - Desktop', 'Neuron', 35000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1024,'Neuron Xtreme DSKTP','Pentium Quad Core/4 GB DDR3/500 GB/Ubuntu - Black, 19.5 Inch Screen', 'Electronics - Desktop', 'Neuron', 32000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1025,'Surface DSKTP','NVD Tegra Quad Core/ 1GB/ Android 4.2/353.8 mm x 530.9 mm x 67 mm, 4.85 kg, 54.61 Inch Screen', 'Electronics - Desktop', 'Macrosoft', 55000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1026,'Surface Pro DSKTP','NVD Tegra Quad Core/ 1GB/ Android 4.2/Touch 353.8 mm x 530.9 mm x 67 mm, 4.85 kg, 54.61 Inch Screen', 'Electronics - Desktop', 'Macrosoft', 75000, 8);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1027,'Raptor Neon DSKTP','Core i5 (6th Gen)/4 GB DDR3/1 TB/Windows 10 Home - White, 23.8 Inch Screen', 'Electronics - Desktop', 'Raptor', 55000, 2);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1028,'Canyon JetCam',' DSLR Camera Body with Single Lens: EF-S 18-55 IS II ', 'Electronics - Camera', 'Canyon', 50000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1029,'Canyon Clix','DSLR Camera Body with Dual Lens: AF-P DX NIKKOR 18 - 55 mm f/3.5 - 5.6G VR + AF-P DX NIKKOR 70 - 300 mm f/4.5 - 6.3G ED VR 16 GB SD Card', 'Electronics - Camera', 'Canyon', 65000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1030,'Canyon clix Plus','DSLR Camera Body with Single Lens: AF-P DX NIKKOR 18-55 mm f/3.5-5.6G VR Kit ', 'Electronics - Camera', 'Canyon', 75000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1031,'Nixon TrueLife','700D DSLR Camera Body with Dual Lens: EF S18 - 55 mm IS II and EF S55 - 250 mm IS II ', 'Electronics - Camera', 'Nixon', 45000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1032,'Nixon Shot','DSLR Camera Body with Single Lens: AF-P DX', 'Electronics - Camera', 'Nixon', 52000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1033,'Nixon ProCam','DSLR Camera Body with Single Lens: EF S18-55 IS STM', 'Electronics - Camera', 'Nixon', 56000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1034,'DigiFilm InFocus','DSLR Camera Body with Lens: AF-P 18-55mm VR + AF-P DX', 'Electronics - Camera', 'DigiFilm', 35000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1035,'DigiFilm Njoy','DSLR Camera Body with AF-S DX NIK 18-105 mm F/3.5-5.6 G ED VR', 'Electronics - Camera', 'DigiFilm', 45000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1036,'DigiFilm Cynosure','77D DSLR Camera Body with Single Lens: EF-S18-135 IS USM', 'Electronics - Camera', 'DigiFilm', 43000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1037,'DigiFilm Roll','DSLR Camera Body with Dual Lens: AF-P DX Nikkor 18 - 55 MM F/3.5-5.6G VR and 70-300 MM F/4.5-6.3G ED VR', 'Electronics - Camera', 'DigiFilm', 35000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1038,'GoodLife Iron-Box','Adjustable thermostat control - Shock-proof plastic body - Non-stick sole plate', 'Electronics - Other Appliances', 'GoodLife', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1039,'GoodLife Microwave Oven','Convection Microwave Oven - Black', 'Electronics - Other Appliances', 'GoodLife', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1040,'JVL BT5 Bluetooth Speaker','Portable Bluetooth Mobile/Tablet Speaker', 'Electronics - Other Appliances', 'JVL', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1041,'Jab 5.1 Speaker','5.1 1 Subwoofer With 5.25"Woofer Unit, 5 Satellite Speakers With Full Range Speaker Units', 'Electronics - Other Appliances', 'Jab', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1042,'Aro RF JeanssM-32', 'Regular Fit Jeans for Men, size-32', 'Clothing - Men', 'ARO', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1043,'Aro RF JeanssM-34','Regular Fit Jeans for Men / Size 34 / Black', 'Clothing - Men', 'ARO', 4500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1044,'Aro RF JeanssM-36','Regular Fit Jeans for Men / Size 36 / Navy Blue', 'Clothing - Men', 'ARO', 2500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1045,'Aro STL JeansM-32','Denim - Stretchable Jeans for Men-32, Skinny fit', 'Clothing - Men', 'ARO', 3500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1046,'Aro STL JeansM-34','Skinny Fit stretchable Jeans for Men/Size-32', 'Clothing - Men', 'ARO', 3100, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1047,'LP FL TrouserM-32','LuiPhilipe formal trouser for men / Size-32', 'Clothing - Men', 'LuiPhilipe', 3600, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1048,'LP FL TrouserM-34','LuiPhilipe formal trouser for men / Size-34', 'Clothing - Men', 'LuiPhilipe', 5200, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1049,'TTL FL ShirtM-38','Formal shirt for men / Size-38', 'Clothing - Men', 'TUTUL', 2500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1050,'TTL FL ShirtM-40','Formal shirt for men / Size-40', 'Clothing - Men', 'TUTUL', 2300, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1051,'TTL FL ShirtM-42','Formal shirt for men / Size-42', 'Clothing - Men', 'TUTUL', 4500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1052,'Melanje kurti-M','Black Cotton Kurti-M', 'Clothing - Women', 'Melanje', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1053,'Beeba Palazzo-L','Royal Blue Silk pallazo-L', 'Clothing - Women', 'Beeba', 4000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1054,'Lyfstyle Trouser-28','Cotton Trousers - 28', 'Clothing - Women', 'Lyfstyle', 6000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1055,'Dabloo Legging-M ','Cotton Legging (grey) size - M', 'Clothing - Women', 'Dabloo', 2000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1056,'Guchi Tunics-M','Denim Tunic (Olive Greeen) Medium Size', 'Clothing - Women', 'Guchi', 4000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1057,'Honee Casual Pant-M','Casual Pant - Off white, Medium size', 'Clothing - Women', 'Honee', 2000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1058,'LateenQuarters Jump Suit-M','Polyster Cotton Jump suit Navy Blue, Size - M', 'Clothing - Women', 'LateenQuarters', 4000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1059,'And Dungaree-M','Cotton Dungaree - Denim', 'Clothing - Women', 'And', 1500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1060,'Silk crafted Saree','Black Saree with brown border, golden crafted, 5m long', 'Clothing - Women', 'Grand Silk', 5000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1061,'Cotton crafted Saree','Black Saree with red border, 5m long, golden crafted', 'Clothing - Women', 'Grand Silk', 4200, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1062,'Silk printed Saree','Red Saree, with white border and prints, 4m long', 'Clothing - Women', 'Grand Silk', 3000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1063,'MSD wicket Keeping Gloves','Leather gloves, drill lining cuff with rubber palm.', 'Sports', 'MSD', 1500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1064,'VSKA Cricket Bat','Jumbo Kashmir Willow Cricket Bat - Short Handle', 'Sports', 'VSKA', 2500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1065,'Mess Football','Hand Stitched Ball Laminated With 3 Layers Of Polyester Cotton', 'Sports', 'Mess', 750, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1066,'Nivi Volleyball','Volleyball, weight-300g', 'Sports', 'Nivi', 500, 550);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1067,'SuperGrip Sports Shoes','Sports shoes with light weight and flexible and extra grip sole', 'Sports', 'SuperGrip', 4200, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1068,'Hawk Fry Pan','Special Induction Base Non-Stick Fry Pan, 24cm', 'Home - Kitchen', 'Hawk', 1200, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1069,'Pigi Pressure Cooker','All Outer Aluminum Pressure Cooker, 5 Litres, Silver', 'Home - Kitchen', 'PIGI', 1500, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1070,'TKW Sofa Set','Sofa 3+1+1 sofa sets are usually ideal for most of the living rooms.', 'Home - Furniture', 'TKW', 50000, 15);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1071,'TKW Side Table','Sheesham Wood Bed Side Cabinet (Natural Honey Finish)', 'Home - Furniture', 'TKW', 15000, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1072,'Hommie Curtain','Artistic floral door curtain (Ring) - 48inch X 90inch.', 'Home - Decor', 'Hommie', 2550, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1073,'RoyaleDecor Curtain','Artistic floral window curtain (Ring) ', 'Home - Decor', 'RoyaleDecor', 1550, 150);
INSERT INTO EK_PRODUCT (PRODUCT_ID, NAME, DESCRIPTION, CATEGORY, BRAND, PRICE,  QUANTITY) VALUES (1074,'RoyaleDecor Bed Lamp','Wooden Table Lamp Rope Style (Size: 25.4 x 17.8 x 17.8 cm)', 'Home - Decor', 'RoyaleDecor', 1089, 200);


commit;
SELECT * FROM EK_PRODUCT;