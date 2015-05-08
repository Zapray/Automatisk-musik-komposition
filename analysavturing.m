%% Analys av turingtest
fileID = fopen('scorereport_1081027.tsv');

formatSpec = '%f %f'; %flyttal

DATA=textscan(fileID,formatSpec,'HeaderLines',0,'delimiter','\t','CollectOutput',1); % data till cell

fclose('all');

data2 = cell2mat(DATA); % cell till matris
plot(data2(:,1),dat

a2(:,2))

%%
