import csv
import random
import string

def random_name(length=8):
    return ''.join(random.choices(string.ascii_letters, k=length))

def random_sign(length=20):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def random_following_list(all_user_ids):
    m = random.randint(1, 100)  # 随机选择列表长度
    return ','.join(map(str, random.sample(all_user_ids, m)))

output_file = 'random_users_data.csv'

total_users = 2000000

user_ids = list(range(1000000, 3000000))  # 用户ID从1000000到2999999，不重复

random.shuffle(user_ids)

with open(output_file, mode='w', encoding='utf-8', newline='') as outfile:
    fieldnames = ["mid", "level", "name", "official_role", "official_title", "rank", "sign", "following_list"]
    writer = csv.DictWriter(outfile, fieldnames=fieldnames)
    
    writer.writeheader()
    count=0
    for mid in user_ids:
        count=count+1
        user_data = {
            "mid": mid,
            "level": random.randint(1, 10),  # 用户等级1到10之间
            "name": random_name(),  # 随机生成的姓名
            "official_role": random.choice([0, 1, 2]),  # 官方认证形式为0, 1, 2
            "official_title": random_name(7),  # 官方认证头衔
            "rank": random.randint(1, 200000000),  # 排名为1到100000000之间的随机数
            "sign": random_sign(),  # 随机生成的签名
            "following_list": random_following_list(user_ids)  # 随机生成的关注列表
        }
        
        writer.writerow(user_data)
        if count%10000==0:
            print(f"写入{count}数据")

print(f"随机数据生成完成并已存储到 {output_file}")