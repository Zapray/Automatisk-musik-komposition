
filename = 'Algoritmisk Komposition Quiz_1431068980';


[tmp, song1, raw] = xlsread(filename, 'Q:S');
[tmp, song2, raw] = xlsread(filename, 'U:W');
[tmp, song3, raw] = xlsread(filename, 'Y:AA');
[tmp, song4, raw] =  xlsread(filename, 'AC:AE');
[tmp, song5, raw] =  xlsread(filename, 'AG:AI');
[tmp, song6, raw] =  xlsread(filename, 'AK:AM');
[tmp, song7, raw] =  xlsread(filename, 'AO:AQ');
[tmp, song8, raw] =  xlsread(filename, 'AS:AU');
[tmp, song9, raw] = xlsread(filename, 'AW:AY');
[tmp, song10, raw] =  xlsread(filename, 'BA:BC');

[tmp, pop, raw] = xlsread(filename, 'H:I');
[tmp, egen, raw] = xlsread(filename, 'K:L');
[tmp, algo, raw] = xlsread(filename, 'N:O');

popON = 0; popStatus = 'Ja';
egenON = 1; egenStatus = 'Ja';
algoON = 1; algoStatus = 'Ja';


count1 = zeros(1,3);
count2 = zeros(1,3);
count3 = zeros(1,3);
count4 = zeros(1,3);
count5 = zeros(1,3);
count6 = zeros(1,3);
count7 = zeros(1,3);
count8 = zeros(1,3);
count9 = zeros(1,3);
count10 = zeros(1,3);

for i = 2:size(song1,1)
    for j = 1:3
        if(~isempty(song1{i,j}))
            
            if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count1(j) = count1(j) + 1;

            end
        end
    end
end


for i = 2:size(song2,1)
    for j = 1:3
        if(~isempty(song2{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count2(j) = count2(j) + 1;

            end
        end
    end
end

for i = 2:size(song3,1)
    for j = 1:3
        if(~isempty(song3{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count3(j) = count3(j) + 1;

            end
        end
    end
end

for i = 2:size(song4,1)
    for j = 1:3
        if(~isempty(song4{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count4(j) = count4(j) + 1;

            end
        end
    end
end

for i = 2:size(song5,1)
    for j = 1:3
        if(~isempty(song5{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count5(j) = count5(j) + 1;

            end
        end
    end
end

for i = 2:size(song6,1)
    for j = 1:3
        if(~isempty(song6{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count6(j) = count6(j) + 1;

            end
        end
    end
end

for i = 2:size(song7,1)
    for j = 1:3
        if(~isempty(song7{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count7(j) = count7(j) + 1;

            end
        end
    end
end

for i = 2:size(song8,1)
    for j = 1:3
        if(~isempty(song8{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count8(j) = count8(j) + 1;

            end
        end
    end
end

for i = 2:size(song9,1)
    for j = 1:3
        if(~isempty(song9{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count9(j) = count9(j) + 1;

            end
        end
    end
end

for i = 2:size(song10,1)
    for j = 1:3
        if(~isempty(song10{i,j}))
             if(popON == 1 && strcmp(pop(i),popStatus))
                
            elseif(egenON == 1 && strcmp(egen(i), egenStatus))
                
            elseif(algoON == 1 && strcmp(algo(i), egenStatus))
                
            else
                count10(j) = count10(j) + 1;

            end
        end
    end
end


%% PLOT
count = [count1;
    count2;
    count3;
    count4;
    count5;
    count6;
    count7;
    count8;
    count9;
    count10];
figure(100)
bar(count)
legend('M?nniska','Dator','H?rt tidigare')


