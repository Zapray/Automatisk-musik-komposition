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