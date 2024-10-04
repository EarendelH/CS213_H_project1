import csv
import ast
import sys

# 增加CSV字段大小限制
csv.field_size_limit(sys.maxsize)

# 输入和输出文件路径
input_file = 'user.csv'  # 你的原始CSV文件路径
output_file = 'processed_user.csv'  # 处理后的CSV文件路径

# 函数：处理follower_list或following_list列，提取mid中的数字并用逗号隔开
def process_list(column_value):
    try:
        # 将字符串转换为Python列表
        data_list = ast.literal_eval(column_value)
        # 提取mid字段中的数字，并去掉任何其他信息
        mid_list = [str(item['mid']) for item in data_list]
        # 用逗号连接所有的mid数字
        return ','.join(mid_list)
    except (ValueError, SyntaxError):
        return ''  # 如果解析失败，返回空字符串

# 处理CSV文件
def process_csv(input_file, output_file):
    with open(input_file, mode='r', encoding='utf-8', errors='ignore') as infile, \
         open(output_file, mode='w', encoding='utf-8', newline='') as outfile:
        
        # 读取原始CSV文件
        reader = csv.DictReader(infile)
        # 获取原始表头
        fieldnames = reader.fieldnames
        # 添加新的字段用于存储处理后的列表
        fieldnames += ['processed_following_list', 'processed_follower_list']
        
        # 创建写入器
        writer = csv.DictWriter(outfile, fieldnames=fieldnames)
        writer.writeheader()
        
        count = 0
        # 遍历每一行数据
        for row in reader:
            if count<200 :continue
            count += 1
            print(f"正在处理第 {count} 行...")
            # 处理following_list和follower_list，提取mid中的数字
            row['processed_following_list'] = process_list(row['following_list'])
            row['processed_follower_list'] = process_list(row['follower_list'])
            print("处理后"+row['processed_following_list'])
            # 将处理后的行写入新文件
            writer.writerow(row)

    print(f"处理完成！已将结果存储到 {output_file}")

# 调用函数进行处理
process_csv(input_file, output_file)