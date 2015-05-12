clear all
filename = 'Algoritmisk Komposition Quiz_1431068980';
nbrOfSongs = 9;

%Songs
[tmp, song1, raw] = xlsread(filename, 'Q:S');
[tmp, song2, raw] = xlsread(filename, 'U:W');
[tmp, song3, raw] = xlsread(filename, 'Y:AA');
[tmp, song4, raw] =  xlsread(filename, 'AC:AE');
[tmp, song5, raw] =  xlsread(filename, 'AG:AI');
[tmp, song6, raw] =  xlsread(filename, 'AK:AM');
[tmp, song7, raw] =  xlsread(filename, 'AO:AQ');
[tmp, song8, raw] =  xlsread(filename, 'AS:AU');
[tmp, song9, raw] = xlsread(filename, 'AW:AY');
%[tmp, song10, raw] =  xlsread(filename, 'BA:BC');

nbrOfPeople = size(song1,1)-1;
songs = cell(nbrOfPeople, 3, nbrOfSongs);
songs(:,:,1) = song1(2:end,:);
songs(:,:, 2) = song2(2:end,:);
songs(:,:, 3) = song3(2:end,:);
songs(:,:, 4) = song4(2:end,:);
songs(:,:, 5) = song5(2:end,:);
songs(:,:, 6) = song6(2:end,:);
songs(:,:, 7) = song7(2:end,:);
songs(:,:, 8) = song8(2:end,:);
songs(:,:, 9) = song9(2:end,:);
%songs(:,:, 10) = song10;

% Questions
[tmp, pop, raw] = xlsread(filename, 'H:I');
[tmp, egen, raw] = xlsread(filename, 'K:L');
[tmp, algo, raw] = xlsread(filename, 'N:O');

pop = pop(2:end);
egen = egen(2:end);
algo = algo(2:end);

popON = 0; popStatus = 'Ja';
egenON = 0; egenStatus = 'Ja';
algoON = 0; algoStatus = 'Ja';

%Score
score =  xlsread(filename, 'B:B');
score = score(1:end);
%Remove all "heard before's"
countHeardBefores = zeros(nbrOfPeople,1);
score = score/10-1;%Remove one cause song nbr 10 was removed
for i = 1:size(score,1)
    for s = 1:nbrOfSongs
        if(~isempty(songs{i, 3, s}))
            countHeardBefores(i) = countHeardBefores(i) + 1;
        end
    end
end
for i = 1:size(score,1)
    score(i) = score(i)/(nbrOfSongs - countHeardBefores(i));
end


%Count all possible scores (use histogram instead)
mapScores = containers.Map('KeyType','double','ValueType','double')
for i = 1:size(score,1)
    if(~isKey(mapScores, score(i)))
        mapScores(score(i)) = 1;
    else
        mapScores(score(i)) = mapScores(score(i)) + 1;
    end
end

%Count f?r each song
count = zeros(nbrOfSongs,3);
for i = 1:size(songs,1)
    for j = 1:size(songs,2)
        for k = 1:size(songs,3)
            if(~isempty(songs{i,j,k}))
                if(popON == 1 && strcmp(pop(i),popStatus))
                    
                elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                    
                elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                    
                else
                    count(k,j) = count(k,j) + 1;
                    
                end
            end
        end
    end
end


%% PLOT
figure(1)
bar(count)
legend('M?nniska','Dator','H?rt tidigare')


figure(2)
%allKeys = cell2mat(keys(mapScores));
%allValues = cell2mat(values(mapScores));
%bar(allKeys, allValues)
nbrOfBins = 10;
histfit(score, nbrOfBins)
std(score)
% A (tex 95%) procentigt konfidensintervall ?r sannolikheten (A=95%) f?r
% att en ny testperson hamnar inom intervallet (2.5% fr?n varje h?ll) Ty
% jag har ett tv?-sidigt konfidensintervall. 
