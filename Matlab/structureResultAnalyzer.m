%% Intro
data = [0, 4, 10, 4, 20, 1, 6, 1;
        4, 0, 6, 9, 1, 21, 3, 5;
        10, 6, 0, 7, 5, 3, 18, 1;
        4, 9, 7, 0, 1, 6, 5, 14;
        20, 1, 5, 1, 0, 2, 6, 1;
        1, 21, 3, 6, 2, 0, 3, 7;
        6, 3, 18, 5, 6, 3, 0, 2;
        1, 5, 1, 14, 1, 7, 2, 0];
songcount = 30;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data)); 
data(logical(eye(size(data)))) = 0;
titlename = 'Intro';
%% Verse
data = [0, 11, 20, 3, 52, 7, 8, 1;
        11, 0, 10, 11, 6, 54, 3, 4;
        20, 10, 0, 10, 9, 6, 44, 4;
        3, 11, 10, 0, 3, 10, 4, 40;
        52, 6, 9, 3, 0, 10, 13, 1;
        7, 54, 6, 10, 10, 0, 7, 3;
        8, 3, 44, 4, 13, 7, 0, 3;
        1, 4, 4, 40, 1, 3, 3, 0];
songcount = 89;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data)); 
titlename = 'Verse';
%% Pre-chorus
data = [0, 2, 8, 0, 25, 3, 3, 0;
                  2, 0, 4, 4, 2, 27, 3, 1;
                  8, 4, 0, 3, 6, 7, 22, 1;
                  0, 4, 3, 0, 0, 1, 3, 16;
                  25, 2, 6, 0, 0, 2, 5, 0;
                  3, 27, 7, 1, 2, 0, 4, 0;
                  3, 3, 22, 3, 5, 4, 0, 3;
                  0, 1, 1, 16, 0, 0, 3, 0];
songcount = 103;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data)); 
titlename = 'Pre-chorus';
%% Chorus

data = [0, 13, 46, 1, 90, 10, 14, 1;
        13, 0, 10, 42, 9, 101, 13, 16;
        46, 10, 0, 8, 28, 6, 91, 6;
        1, 42, 8, 0, 5, 24, 12, 73;
        90, 9, 28, 5, 0, 13, 27, 2;
        10, 101, 6, 24, 13, 0, 15, 20;
        14, 13, 91, 12, 27, 15, 0, 11;
        1, 16, 6, 73, 2, 20, 11, 0];
songcount = 176;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data));  



titlename = 'Chorus';
%% Bridge
data = [0, 5, 7, 1, 14, 2, 1, 0;
        5, 0, 5, 3, 2, 13, 1, 1;
        7, 5, 0, 2, 3, 2, 6, 2;
        1, 3, 2, 0, 0, 2, 1, 7;
        14, 2, 3, 0, 0, 4, 3, 0;
        2, 13, 2, 2, 4, 0, 2, 1;
        1, 1, 6, 1, 3, 2, 0, 4;
        0, 1, 2, 7, 0, 1, 4, 0];
songcount = 21;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data)); 
titlename = 'Bridge';

%% Our songs

data = [0, 20, 40, 6, 80, 39, 39, 22;
20, 0, 11, 37, 18, 45, 24, 33;
40, 11, 0, 9, 19, 10, 40, 11;
6, 37, 9, 0, 6, 23, 5, 23;
80, 18, 19, 6, 0, 31, 37, 14;
39, 45, 10, 23, 31, 0, 16, 23;
39, 24, 40, 5, 37, 16, 0, 12;
22, 33, 11, 23, 14, 23, 12, 0];
songcount = 244;
data(logical(eye(size(data)))) = songcount;
data = data/max(max(data)); 
titlename = 'Genererad musik';
%% 
% figure(1)
% b = bar3(data/max(max(data)));
% for k = 1:length(b)
%     zdata = b(k).ZData;
%     b(k).CData = zdata;
%     b(k).FaceColor = 'interp';
% end
% colorbar
% title(titlename);
% colormap gray 
%  
% %zlim([0 0.3])
% ylim([0.5 8.5])
% xlim([0.5 8.5])
colorMatrix=[];
for w = 1:8
    for v=1:8
        colorMatrix = [colorMatrix; 1-data(v,w) 1-data(v,w) 1-data(v,w)];
    end
end


ind=1;
for u=0:8-1
    for c=0:8-1
        rectangle('Position',[u c u+1 c+1],'FaceColor',colorMatrix(ind,:))
        hold on
        ind=ind+1;
    end
end
%Set the axis correct
ytick = zeros(8+1,1);
xtick = zeros(8,1);
for i=1:8+1
    ytick(i)=i-0.5;
end
for i=1:8
    xtick(i)=i-0.5;
end


title(titlename)
ax = gca;
ax.YTick = ytick;
ax.XTick=xtick;
set(ax,'yticklabel',1:8);
set(ax,'xticklabel',1:8);
axis([0 8 0 8])

colormap(flipud(gray(82)))
colorbar
