package com.example.mcp.food.repository;

import com.example.mcp.food.model.MenuItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 菜单数据仓库类 - 负责菜单数据的存储和查询
 *
 * 知识点：
 * - @Repository: Spring 数据访问层注解，标识这是一个数据仓库组件
 * - ConcurrentHashMap: 线程安全的 Map，用于多线程环境下的数据存储
 * - @PostConstruct: 在 Bean 初始化后执行的方法，用于初始化数据
 */
@Repository
public class MenuRepository {

    // 使用 ConcurrentHashMap 存储菜单项，key 为菜品 ID，value 为 MenuItem 对象
    private final Map<String, MenuItem> menuItems = new ConcurrentHashMap<>();

    /**
     * 初始化菜单数据 - 在 Bean 初始化后自动执行
     */
    @PostConstruct
    public void initMenuData() {
        // 初始化一些示例菜品
        addMenuItem(MenuItem.builder()
                .id("M001")
                .name("宫保鸡丁")
                .description("经典川菜，鸡肉丁配以花生米、干辣椒，麻辣鲜香")
                .price(38.0)
                .category("川菜")
                .available(true)
                .imageUrl("https://example.com/images/gongbaojiding.jpg")
                .build());

        addMenuItem(MenuItem.builder()
                .id("M002")
                .name("麻婆豆腐")
                .description("四川名菜，豆腐嫩滑，麻辣味厚，香气扑鼻")
                .price(28.0)
                .category("川菜")
                .available(true)
                .imageUrl("https://example.com/images/mapodoufu.jpg")
                .build());

        addMenuItem(MenuItem.builder()
                .id("M003")
                .name("白切鸡")
                .description("粤菜经典，鸡肉鲜嫩，配以姜葱蘸料")
                .price(45.0)
                .category("粤菜")
                .available(true)
                .imageUrl("https://example.com/images/baiqieji.jpg")
                .build());

        addMenuItem(MenuItem.builder()
                .id("M004")
                .name("扬州炒饭")
                .description("粒粒分明，配料丰富的经典炒饭")
                .price(25.0)
                .category("主食")
                .available(true)
                .imageUrl("https://example.com/images/yangzhouchaofan.jpg")
                .build());

        addMenuItem(MenuItem.builder()
                .id("M005")
                .name("芒果布丁")
                .description("香甜芒果味，口感滑嫩的甜点")
                .price(18.0)
                .category("甜点")
                .available(true)
                .imageUrl("https://example.com/images/mangobuding.jpg")
                .build());

        addMenuItem(MenuItem.builder()
                .id("M006")
                .name("清蒸鲈鱼")
                .description("粤菜精品，鱼肉鲜嫩，原汁原味")
                .price(88.0)
                .category("粤菜")
                .available(false)
                .imageUrl("https://example.com/images/qingzhengluyu.jpg")
                .build());
    }

    /**
     * 添加菜单项
     *
     * @param menuItem 菜单项对象
     * @return 添加后的菜单项
     */
    public MenuItem addMenuItem(MenuItem menuItem) {
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }

    /**
     * 根据 ID 查询菜单项
     *
     * @param id 菜品 ID
     * @return 菜单项，如果不存在返回 null
     */
    public MenuItem getMenuItemById(String id) {
        return menuItems.get(id);
    }

    /**
     * 查询所有菜单项
     *
     * @return 所有菜单项列表
     */
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItems.values());
    }

    /**
     * 根据分类查询菜单项
     *
     * @param category 分类名称
     * @return 该分类下的菜单项列表
     */
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItems.values().stream()
                .filter(item -> category.equalsIgnoreCase(item.getCategory()))
                .collect(Collectors.toList());
    }

    /**
     * 查询所有可用的菜单项
     *
     * @return 可用菜单项列表
     */
    public List<MenuItem> getAvailableMenuItems() {
        return menuItems.values().stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * 根据名称模糊查询菜单项
     *
     * @param name 菜品名称关键词
     * @return 匹配的菜单项列表
     */
    public List<MenuItem> searchMenuItemsByName(String name) {
        return menuItems.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 更新菜单项
     *
     * @param menuItem 更新后的菜单项
     * @return 更新后的菜单项，如果不存在返回 null
     */
    public MenuItem updateMenuItem(MenuItem menuItem) {
        if (menuItems.containsKey(menuItem.getId())) {
            menuItems.put(menuItem.getId(), menuItem);
            return menuItem;
        }
        return null;
    }

    /**
     * 删除菜单项
     *
     * @param id 菜品 ID
     * @return 被删除的菜单项，如果不存在返回 null
     */
    public MenuItem deleteMenuItem(String id) {
        return menuItems.remove(id);
    }

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    public Set<String> getAllCategories() {
        return menuItems.values().stream()
                .map(MenuItem::getCategory)
                .collect(Collectors.toSet());
    }
}
