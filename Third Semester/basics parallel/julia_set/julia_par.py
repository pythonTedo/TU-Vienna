from re import U
import numpy as np
import matplotlib.pyplot as plt
import argparse
import time
import os
from multiprocessing import Pool, TimeoutError
from julia_curve import c_from_group
from prettytable import PrettyTable
import math

# Update according to your group size and number (see TUWEL)
GROUP_SIZE   = 2
GROUP_NUMBER = 7

# do not modify BENCHMARK_C
BENCHMARK_C = complex(-0.2, -0.65)

def compute_julia_set_sequential(xmin, xmax, ymin, ymax, im_width, im_height, c):
    
#    print('pid {}'.format(os.getpid()))
    
    zabs_max = 10
    nit_max = 300

    xwidth  = xmax - xmin
    yheight = ymax - ymin

    julia = np.zeros((im_width, im_height))
    for ix in range(im_width):
        for iy in range(im_height):
            nit = 0
            # Map pixel position to a point in the complex plane
            z = complex(ix / im_width * xwidth + xmin,
                        iy / im_height * yheight + ymin)
            # Do the iterations
            while abs(z) <= zabs_max and nit < nit_max:
                z = z**2 + c
                nit += 1
            ratio = nit / nit_max
            julia[ix,iy] = ratio

    return julia

def compute_patch(args):
    x, y, xmin, xmax, ymin, ymax, size, xmin_of, xmax_of, ymin_of, ymax_of, c = args.values()

    zabs_max = 10
    nit_max = 300

    xwidth = xmax - xmin
    yheight = ymax - ymin

    xwidth_patch = xmax_of - xmin_of
    yheight_patch = ymax_of - ymin_of

    julia = np.zeros((xwidth_patch, yheight_patch))
    for ix in range(xwidth_patch):
        for iy in range(yheight_patch):
            nit = 0
            z = complex((ix + xmin_of) / size * xwidth + xmin,
                        (iy + ymin_of) / size * yheight + ymin)
            # Do the iterations
            while abs(z) <= zabs_max and nit < nit_max:
                z = z ** 2 + c
                nit += 1
            ratio = nit / nit_max
            julia[ix, iy] = ratio

    return ((x,y),julia)
            
def arrange_grid(size, patch, patches):
    """ Arrange the patches into a grid """
    julia_img = np.zeros((size, size))
    for x in range(len(patches)):
        (x,y), patch_result = patches[x]
        xmin = x * patch
        ymin = y * patch
        julia_img[xmin:xmin+patch, ymin:ymin+patch] = patch_result
    return julia_img

def compute_julia_in_parallel(size, xmin, xmax, ymin, ymax, patch, nprocs, c):

    task_list = []
    n_patch = int(math.ceil(size/patch))
    patch_grid = np.zeros((n_patch,n_patch))
    pool = Pool(processes=nprocs)

    for x in range(patch_grid.shape[0]):
        xmin_of = x * patch
        xmax_of = (x + 1) * patch
        if xmax_of > size:
            xmax_of = size
        for y in range(patch_grid.shape[1]):
            ymin_of = y * patch
            ymax_of = (y + 1) * patch
            if ymax_of > size:
                ymax_of = size
            task_list.append({'x': x,
                              'y': y,
                              'xmin': xmin,
                              'xmax': xmax,
                              'ymin': ymin,
                              'ymax': ymax,
                              'size': size,
                              'xmin_of': xmin_of,
                              'xmax_of': xmax_of,
                              'ymin_of': ymin_of,
                              'ymax_of': ymax_of,
                              'c': c})

    completed_patches = pool.map(compute_patch, task_list, 1)
    pool.close()
    pool.join()

    julia_img = arrange_grid(size, patch, completed_patches)

    return julia_img


if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument("--size", help="image size in pixels (square images)", type=int, default=500)
    parser.add_argument("--xmin", help="", type=float, default=-1.5)
    parser.add_argument("--xmax", help="", type=float, default=1.5)
    parser.add_argument("--ymin", help="", type=float, default=-1.5)
    parser.add_argument("--ymax", help="", type=float, default=1.5)
    parser.add_argument("--group-size", help="", type=int, default=None)
    parser.add_argument("--group-number", help="", type=int, default=None)
    parser.add_argument("--patch", help="patch size in pixels (square images)", type=int, default=20)
    parser.add_argument("--nprocs", help="number of workers", type=int, default=1)
    parser.add_argument("--draw-axes", help="Whether to draw axes", action="store_true")
    parser.add_argument("-o", help="output file")
    parser.add_argument("--benchmark", help="Whether to execute the script with the benchmark Julia set", action="store_true")
    parser.add_argument("--ex2_1", help="Execute number excercise 2.1", action="store_true")
    parser.add_argument("--ex2_2", help="Execute number excercise 2.2", action="store_true")
    parser.add_argument("--ex2_3", help="Execute number excercise 2.3", action="store_true")
    parser.add_argument("--ex2_4", help="Execute number excercise 2.4", action="store_true")
    args = parser.parse_args()

    #print(args)
    if args.group_size is not None:
        GROUP_SIZE = args.group_size
    if args.group_number is not None:
        GROUP_NUMBER = args.group_number
    c = None

    if args.benchmark:
        c = BENCHMARK_C 
    else:
        c = c_from_group(GROUP_SIZE, GROUP_NUMBER) 

    if args.ex2_1:
        stime = time.perf_counter()
        julia_img = compute_julia_in_parallel(
            args.size,
            args.xmin, args.xmax, 
            args.ymin, args.ymax, 
            args.patch,
            args.nprocs,
            c)
        rtime = time.perf_counter() - stime
        print(f"{args.size};{args.patch};{args.nprocs};{rtime}")


    for i in range(2):
        if i==1:
            c=c_from_group(GROUP_SIZE, GROUP_NUMBER)
            name="Cs"
        else:
            c=BENCHMARK_C
            name="Cb"
    
        if args.ex2_2:
            table = PrettyTable()
            table.field_names = ["Size", "Processes", "mean runtime(s)", "speed-up", "parallel efficiency"]
            process_array = [1, 2, 4, 8, 16, 24, 32]
            size_array = [160,1050]
            speed_up_benchmark = 0
            parallel_eff = 0
            patch=25
            
            for size in size_array:
                for process in process_array:
                    mean_runtime = 0
                    for i in range(3):
                        stime = time.perf_counter()
                        julia_img = compute_julia_in_parallel(
                            size,
                            args.xmin, args.xmax, 
                            args.ymin, args.ymax, 
                            patch,
                            process,
                            c)
                                
                        rtime = time.perf_counter() - stime
                        mean_runtime += rtime
                        
                    mean_runtime = mean_runtime / 3
                    if process == 1:
                        speed_up_benchmark = mean_runtime 
                    speed_up = speed_up_benchmark / mean_runtime 
                    parallel_eff = speed_up_benchmark / ( process * mean_runtime ) 
                    table.add_row([size, process, mean_runtime, speed_up, parallel_eff])
                    print("proc--" + str(process) + "--size--" + str(size))
            print(name)    
            print(table)
            table_str = table.get_string(header=False)
            rows = table_str.split("\n")
            data = [row.strip().split("|")[1:6] for row in rows if row.strip()]
            data = [[int(d[0].strip()), int(d[1].strip()), float(d[2].strip()), float(d[3].strip()), float(d[4].strip())] for d in data if len(d) == 5]

            x = [d[0] for d in data]
            y = [d[1] for d in data]
            z = [d[2] for d in data]
            s = [d[3] for d in data]
            q = [d[4] for d in data]
            
            """             colors = ['r' if size == 160 else 'b' for size in x]
            # Create a bar chart
            plt.xticks(rotation=45)
            plt.bar(range(len(data)), z, color=colors)
            plt.xticks(range(len(data)), [f"S:{a}, Pr:{b}" for a, b in zip(x, y)])
            plt.xlabel("Size, Processes")
            plt.ylabel("Mean Runtime (s)")
            plt.title(f"Mean Runtime vs. Size and Processes for{name}")
            plt.show()

            fig, ax = plt.subplots()
            plt.xticks(rotation=25)
            ax.bar(range(len(data)), s, color=colors)
            ax.set_xticks(range(len(data)))
            ax.set_xticklabels([f"S:{a}, Pr:{b}" for a, b in zip(x, y)])
            plt.title(f"Speed-up vs. Size and Processes for{name}")
            ax.set_xlabel
            plt.show()

            fig, ax = plt.subplots()
            plt.xticks(rotation=25)
            ax.bar(range(len(data)), q, color=colors)
            ax.set_xticks(range(len(data)))
            ax.set_xticklabels([f"S:{a}, Pr:{b}" for a, b in zip(x, y)])
            plt.title(f"efficency vs. Size and Processes for{name}")
            ax.set_xlabel
            plt.show() """


    if args.ex2_3:
        table2 = PrettyTable()
        table2.field_names = ["Size", "Patch Size", "Processes", "mean runtime(s)"]
        patch_array = [1, 5, 10, 20, 55, 150, 400]
        size=750
        process=32
    
        for patch in patch_array:
            mean_runtime = 0
            for i in range(3):
                stime = time.perf_counter()
                julia_img = compute_julia_in_parallel(
                    size,
                    args.xmin, args.xmax, 
                    args.ymin, args.ymax, 
                    patch,
                    process,
                    c)              
                rtime = time.perf_counter() - stime
                mean_runtime += rtime
            mean_runtime = mean_runtime / 3
            table2.add_row([size, patch, process, mean_runtime])
            print("proc--" + str(process) + "--size--" + str(size) + "--patch--" + str(patch))

        print(name)    
        print(table2)
        table_str = table2.get_string(header=False)
        rows = table_str.split("\n")
        data2 = [row.strip().split("|")[1:5] for row in rows if row.strip()]
        data2 = [[int(d[0].strip()), int(d[1].strip()), int(d[2].strip()), float(d[3].strip())] for d in data2 if len(d) == 4]

        x2 = [d[1] for d in data2]
        y2 = [d[0] for d in data2]
        z2 = [d[3] for d in data2]

        """         fig, ax = plt.subplots()
        ax.plot(x2, z2, 'bo-', label=f'{name} ({process} processes)')
        ax.set_xlabel('Patch Size')
        ax.set_ylabel('Mean Runtime (s)')
        ax.set_title(f'Mean Runtime vs. Patch Size for {name} (size={size}, {process} processes)')
        ax.legend()
        plt.show() """


    if args.ex2_4:
        table2 = PrettyTable()
        table2.field_names = ["Size", "Patch Size", "Processes", "mean runtime(s)"]
        patch_array = range(1,30)
        size=700
        process=16
    
        for patch in patch_array:
            mean_runtime = 0
            for i in range(3):
                stime = time.perf_counter()
                julia_img = compute_julia_in_parallel(
                    size,
                    args.xmin, args.xmax, 
                    args.ymin, args.ymax, 
                    patch,
                    process,
                    c)              
                rtime = time.perf_counter() - stime
                mean_runtime += rtime
            mean_runtime = mean_runtime / 3
            table2.add_row([size, patch, process, mean_runtime])
            print("proc--" + str(process) + "--size--" + str(size) + "--patch--" + str(patch))

        print(name)
        print(table2)
        table_str = table2.get_string(header=False)
        rows = table_str.split("\n")
        data2 = [row.strip().split("|")[1:5] for row in rows if row.strip()]
        data2 = [[int(d[0].strip()), int(d[1].strip()), int(d[2].strip()), float(d[3].strip())] for d in data2 if len(d) == 4]

        x2 = [d[1] for d in data2]
        y2 = [d[0] for d in data2]
        z2 = [d[3] for d in data2]

"""         fig, ax = plt.subplots()
        ax.plot(x2, z2, 'bo-', label=f'{name} ({process} processes)')
        ax.set_xlabel('Patch Size')
        ax.set_ylabel('Mean Runtime (s)')
        ax.set_title(f'Mean Runtime vs. Patch Size for {name} (size={size}, {process} processes)')
        ax.legend()
        plt.show() """















