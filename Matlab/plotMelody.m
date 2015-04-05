function plotMelody(varargin)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here


input = zeros(size(varargin,2), size(varargin{1},2));
%Input
for i = 1:size(varargin,2)
    arg = varargin{i};
    if isnumeric(arg)
        input(i,:) = arg;
    else
        error('plotMelody cannot decypher arg(%d)', i);
    end
end

figure(1)
clf
for i = 1:size(input,1)
    subplot(size(input,1),1,i)
    plot(input(i,:), 'bo')
    title(['Section: ', num2str(i)])
    grid on
    ax = gca;
    ax.XTick = linspace(0,size(input,2), 5);
    ax.XTickLabel = {'0','1', '2', '3','4'};%, '5', '6', '7', '8'};
    axis([0 size(input,2) 0 100]) 
end


