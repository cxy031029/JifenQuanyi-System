local ok = redis.call('SET', KEYS[1], '1', 'NX', 'EX', ARGV[1])
if ok then
    return 1
end
return 0
