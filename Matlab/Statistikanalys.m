%% Number of chords in a song
%Start with importing second column in statistikantalsangermedackord

totalNbrOfSongs = 525;
nbrOfChords = AntalAckord;

%Convertallvalues to %

for i=1:length(nbrOfChords)
    nbrOfChords(i) = nbrOfChords(i)/totalNbrOfSongs;
end

% Pick out the i largest used chords
j=10;
jLargestUsedChords = zeros(j,2); 

for i=1:j
    jLargestUsedChords(i,2) = max(nbrOfChords);
    k = find(nbrOfChords == max(nbrOfChords));
    jLargestUsedChords(i,1)=k;
    nbrOfChords(k)=0;
end

jLargestUsedChords=round(jLargestUsedChords,2);

% Get the name of the chords
namesOfChords = cell(j,1);
for p=1:length(jLargestUsedChords)
    namesOfChords(p) = Ackord(jLargestUsedChords(p,1));    
end 
bar(jLargestUsedChords(:,2));

set(gca,'xticklabel',namesOfChords);


%% Most used progressions
%Start with importing second column in statistikantalsangermedackord.txt

nbrOfChordProg = antalfoljder;
TotalnbrOfChords=84;
% Choose nbr of chordprogingraph in graph, group others;


sumtotalperChord = zeros(84,1);
e=84;
for s = 1:TotalnbrOfChords
    sumtotalperChord(s)=sum(nbrOfChordProg(e-83:e,1));
    e=e+84;
end


nbrOfChords = 10;

namesOfChords = cell(nbrOfChords,1);
squares = ones(nbrOfChords,nbrOfChords);


%Plocka ut f�ljderna nbrOfChords vanligaste f�ljderna f�r C. Anv�nd samma
%ackord f�r alla.

cChords = nbrOfChordProg(1:24,1);
largUsedChordProg = zeros(nbrOfChords+1,2);


for q = 1:nbrOfChords
    largUsedChordProg(q,2) = max(cChords);
    k = find(cChords == max(cChords));
    largUsedChordProg(q,1)=k;
    cChords(k)=0;
end

cChords=[cChords;nbrOfChordProg(25:84,1)];
otherC=sum(cChords(:,1));
largUsedChordProg(end,2)=otherC;
%Create stringvector with titles
stringChord = cell(nbrOfChords+1,1);

for h = 1:length(largUsedChordProg)-1
    stringChord(h) = Ackord(largUsedChordProg(h,1));
    largUsedChordProg(h,2)=largUsedChordProg(h,2)/sumtotalperChord(1,1);
    
end
stringChord(end)= cellstr('Other');
largUsedChordProg(end,2)=largUsedChordProg(end,2)/sumtotalperChord(1,1);





ax = gca;
ax.YTick =[0.5,1.5,2.5,3.5,4.5,5.5];


mydata=bar(squares,1,'stacked');
ax = gca;
ytick = zeros(nbrOfChords,1);

for i=1:nbrOfChords
    ytick(i)=i-0.5;
end
for p=1:nbrOfChords
    namesOfChords(p) = Ackord(p,1);    
end 



ax.YTick = ytick;
set(ax,'yticklabel',namesOfChords);
set(ax,'xticklabel',namesOfChords);
colormap(ColorOrder2);



%% Analyze statick for chord progressions
clf;
%Start with importing second column in statistikantalsangermedackord.txt
nbrOfChordProg = antalfoljder;
TotalnbrOfChords=84;

% Choose nbr of chordprogingraph in graph, group others;
nbrOfChords = 10;

% Calculate the total of progressions from everychord
sumtotalperChord = zeros(84,1);
e=TotalnbrOfChords;
for s = 1:TotalnbrOfChords
    sumtotalperChord(s)=sum(nbrOfChordProg(e-83:e,1));
    e=e+84;
end
% To choose which chords to use, use the most used chordprogressions for c
cChords = nbrOfChordProg(1:24,1);
largUsedChordProg = zeros(nbrOfChords+1,2);

for q = 1:nbrOfChords
    largUsedChordProg(q,2) = max(cChords);
    k = find(cChords == max(cChords));
    largUsedChordProg(q,1)=k;
    cChords(k)=0;
end

cChords=[cChords;nbrOfChordProg(25:84,1)];
otherC=sum(cChords(:,1));
largUsedChordProg(end,2)=otherC;

stringChord = cell(nbrOfChords+1,1); %Create stringvector with titles

for h = 1:length(largUsedChordProg)-1
    stringChord(h) = Ackord(largUsedChordProg(h,1));
    largUsedChordProg(h,2)=largUsedChordProg(h,2)/sumtotalperChord(1,1); 
end
stringChord(end)= cellstr('Other');
largUsedChordProg(end,2)=largUsedChordProg(end,2)/sumtotalperChord(1,1);

% Do it for the rest of the chords
chordStat=largUsedChordProg(:,2);
statForChords=zeros(nbrOfChords+1,1);

for f = 2:nbrOfChords
    statForChords=zeros(nbrOfChords+1,1);
    index=largUsedChordProg(f,1);
    chord=nbrOfChordProg(index*84-83:index*84,1);
    summa=sum(chord);
    for g = 1:nbrOfChords
        statForChords(g)=chord(largUsedChordProg(g,1))/summa;
        chord(largUsedChordProg(g,1))=0;
    end
    statForChords(nbrOfChords+1)=sum(chord)/summa;
    chordStat=[chordStat statForChords];
end

%bar(chordStat)
rg=255;
%Make stats into colors
colorMatrix=[];
for w =1:length(chordStat(1,:))
    for v=1:nbrOfChords+1
        colorMatrix = [colorMatrix; 1-chordStat(v,w) 1-chordStat(v,w) 1-chordStat(v,w)];
    end
end

%create the squares by drawing them instead
ind=1;
for u=0:nbrOfChords-1
    for c=0:nbrOfChords
    rectangle('Position',[u c u+1 c+1],'FaceColor',colorMatrix(ind,:))
    hold on
    ind=ind+1;
    end
end
%Set the axis correct
ytick = zeros(nbrOfChords+1,1);
xtick = zeros(nbrOfChords,1);
for i=1:nbrOfChords+1
    ytick(i)=i-0.5;
end
for i=1:nbrOfChords
    xtick(i)=i-0.5;
end



ax = gca;
ax.YTick = ytick;
ax.XTick=xtick;
set(ax,'yticklabel',stringChord);
set(ax,'xticklabel',stringChord(1:10,1));

colorbar

%%

% Create the nx(n+1) squarepattern for graph use
squares = ones(nbrOfChords,nbrOfChords+1);
ax = gca;
%ax.YTick =[0.5,1.5,2.5,3.5,4.5,5.5,6.5,7.5,8.5,9.5,10.5,11.5];
ind =1;
%for u=1:nbrOfChords
    %for c=1:nbrOfChords+1
    %bar(u,squares(u,:),1,'stacked','facecolor',colorMatrix(ind,:));
    %hold on
    %ind=ind+1;
    %end
%end


mydata=bar(squares,1,'stacked');
ax = gca;
%set(mydata(1),'FaceColor',[0,1,0])
%set(mydata(2),'FaceColor',[1,1,0])

ytick = zeros(nbrOfChords+1,1);

for i=1:nbrOfChords+1
    ytick(i)=i-0.5;
end
for p=1:nbrOfChords
    namesOfChords(p) = stringChord(p,1);    
end 



ax.YTick = ytick;
set(ax,'yticklabel',namesOfChords);
set(ax,'xticklabel',namesOfChords);
colormap(colorMatrix);


%%  Testbar
clf;
Y = [1 1 1;3 2 1;1 1 1];
X=[1,2,3];

colorm=[0 0 0; 0.5 0.5 0.5; 0.75 0.75 0.75];
%bar(X(1),Y,1,'stacked')
bar(1,Y(1:3),'r')
hold on
bar(2,y(3),'b')



%bar(1,data(2),1,'stacked')
%colormap(colorm)

%%
y = [4.2; 4.6; 5];                  %The data.
s = [.3; .2; .6];                   %The standard deviation.
fHand = figure;
aHand = axes('parent', fHand);
hold(aHand, 'on')
colors = hsv(numel(y));
for i = 1:numel(y)
    bar(i, y(i), 'parent', aHand, 'facecolor', colors(i,:));
end
set(gca, 'XTick', 1:numel(y), 'XTickLabel', {'R0', 'R1', 'R2'})
errorbar(y,s,'r');